package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationColumns
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationData
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel

class ConversationHandler(
    private val context: Context
) {

    val data = ConversationData(context)

    fun loadConversations(inboxId: Long, pin: Pin): List<ConversationModel> {
        return data.getConversations(where = hashMapOf(ConversationColumns.INBOX_ID to inboxId.toString())).mapNotNull {

            val key = AesKeyGenerator.generateKey(pin.toString(), it.salt)

            val publicKeyText =
                AesEncryptor.decryptMessage(it.publicKeyEncrypted, key, it.publicKeyIv) ?: return@mapNotNull  null
            val labelText=
                AesEncryptor.decryptMessage(it.labelEncrypted, key, it.labelIv) ?: return@mapNotNull null

            ConversationModel(
                conversationId = it.conversationId,
                publicKey = PublicKey.parse(publicKeyText) ?: return@mapNotNull null,
                label = Label.create(labelText) ?: return@mapNotNull null,
                hasNewMessage = it.hasNewMessage
            )
        }
    }

    fun delete(inboxId: Long) {
        data.delete(inboxId)
    }


}