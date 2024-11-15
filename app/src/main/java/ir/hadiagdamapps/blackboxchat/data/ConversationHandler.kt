package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationColumns
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationData
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxData
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationEncryptedModel
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel

class ConversationHandler(
    context: Context
) {

    val data = ConversationData(context)
    val inboxData = InboxData(context)

    fun loadConversations(inboxId: Long, pin: Pin): List<ConversationModel> {
        return data.getConversations(where = hashMapOf(ConversationColumns.INBOX_ID to inboxId.toString()))
            .mapNotNull {

                val key = AesKeyGenerator.generateKey(pin.toString(), it.salt)

                val publicKeyText =
                    AesEncryptor.decryptMessage(it.publicKeyEncrypted, key, it.publicKeyIv)
                        ?: return@mapNotNull null
                val labelText =
                    AesEncryptor.decryptMessage(it.labelEncrypted, key, it.labelIv)
                        ?: return@mapNotNull null

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

    fun getPublicKeyByInboxId(inboxId: Long): PublicKey? {
        inboxData.getInboxes(hashMapOf(InboxColumns.INBOX_ID to inboxId.toString())).apply {
            return if (this.isEmpty()) null
            else this[0].inboxPublicKey
        }
    }

    fun newConversation(publicKey: PublicKey, pin: Pin, inboxId: Long): ConversationModel {

        if(getPublicKeyByInboxId(inboxId) == publicKey)
            throw Error.SAME_PUBLIC_KEY_CONVERSATION

        val salt = AesKeyGenerator.generateSalt()
        val key = AesKeyGenerator.generateKey(pin.toString(), salt)
        val label = Label.create(publicKey)

        val (encryptedPublicKey, publicKeyIv) = AesEncryptor.encryptMessage(
            message = publicKey.display(),
            aesKey = key
        )

        val (encryptedLabel, labelIv) = AesEncryptor.encryptMessage(
            message = label.display(),
            aesKey = key
        )

        val model = ConversationEncryptedModel(
            conversationId = -1,
            publicKeyEncrypted = encryptedPublicKey,
            labelEncrypted = encryptedLabel,
            hasNewMessage = false,
            publicKeyIv = publicKeyIv,
            labelIv = labelIv,
            salt = salt
        )

        return ConversationModel(
            conversationId = data.insert(model, inboxId),
            publicKey = publicKey,
            label = label,
            hasNewMessage = false
        )
    }

}