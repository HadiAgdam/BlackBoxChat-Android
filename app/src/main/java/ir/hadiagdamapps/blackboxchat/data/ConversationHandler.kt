package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
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

    private val data = ConversationData(context)
    private val inboxData = InboxData(context)

    private fun ConversationEncryptedModel.decryptConversation(pin: Pin): ConversationModel? {
        AesKeyGenerator.generateKey(pin.toString(), salt).apply {
            return ConversationModel(
                conversationId = conversationId, publicKey = PublicKey.parse(
                    AesEncryptor.decryptMessage(publicKeyEncrypted, this, publicKeyIv)
                        ?: return null
                ) ?: return null, label = Label.create(
                    AesEncryptor.decryptMessage(labelEncrypted, this, labelIv) ?: return null
                ) ?: return null, hasNewMessage = hasNewMessage
            )
        }
    }

    fun loadConversations(inboxId: Long, pin: Pin): List<ConversationModel> {
        return data.getConversationsByInboxId(inboxId).mapNotNull {
            it.decryptConversation(pin)
        }
    }

    fun delete(inboxId: Long) {
        data.deleteByConversationId(inboxId)
    }

    private fun getPublicKeyByInboxId(inboxId: Long): PublicKey? {
        inboxData.getInboxes(hashMapOf(InboxColumns.INBOX_ID to inboxId.toString())).apply {
            return firstOrNull()?.inboxPublicKey
        }
    }

    fun newConversation(publicKey: PublicKey, pin: Pin, inboxId: Long): ConversationModel {

        if (getPublicKeyByInboxId(inboxId) == publicKey)
            throw Error.SAME_PUBLIC_KEY_CONVERSATION

        if(loadConversations(inboxId, pin).indexOfFirst{ it.publicKey == publicKey } != -1)
            throw Error.NOT_UNIQUE_PUBLIC_KEY_CONVERSATION

        val salt = AesKeyGenerator.generateSalt()
        val key = AesKeyGenerator.generateKey(pin.toString(), salt)
        val label = Label.create(publicKey)

        val (encryptedPublicKey, publicKeyIv) = AesEncryptor.encryptMessage(
            message = publicKey.display(),
            aesKey = key
        )

        val (encryptedLabel, labelIv) = AesEncryptor.encryptMessage(
            message = label.display(), aesKey = key
        )

        val model = ConversationEncryptedModel(
            conversationId = -1,
            publicKeyEncrypted = encryptedPublicKey,
            labelEncrypted = encryptedLabel,
            hasNewMessage = true,
            publicKeyIv = publicKeyIv,
            labelIv = labelIv,
            salt = salt
        )

        return ConversationModel(
            conversationId = data.insert(model, inboxId),
            publicKey = publicKey,
            label = label,
            hasNewMessage = true
        )
    }

    fun updateLabel(newLabel: Label, pin: Pin, conversationId: Long) {
        AesEncryptor.encryptMessage(
            newLabel.display(), AesKeyGenerator.generateKey(
                pin.toString(),
                data.getSalt(conversationId) ?: throw Error.CONVERSATION_ID_NOT_FOUND
            )
        ).apply {
            data.updateLabel(
                conversationId = conversationId,
                newLabel = first,
                newLabelIv = second
            )
        }
    }

    fun getConversationByPublicKey(
        publicKey: PublicKey,
        pin: Pin,
        inboxId: Long
    ): ConversationModel? =
        loadConversations(inboxId, pin).firstOrNull { it.publicKey == publicKey }


}