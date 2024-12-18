package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.message.MessageData
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.message.EncryptedLocalMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.LocalMessage

class LocalMessageHandler(
    private val context: Context,
    private val conversationId: Long,
    private val pin: Pin,
    private val salt: String
) {

    private val messageData = MessageData(context)
    private val aesKey = AesKeyGenerator.generateKey(pin.toString(), salt)

    fun loadMessages(): List<LocalMessage> {
        return messageData.getByConversationId(conversationId).mapNotNull {
            LocalMessage(
                messageId = it.messageId,
                conversationId = conversationId,
                text = AesEncryptor.decryptMessage(
                    encryptedMessage = it.encryptedText,
                    aesKey = aesKey,
                    ivString = it.iv
                ) ?: return@mapNotNull null,
                sent = it.sent
            )
        }
    }

    fun newMessage(
        message: LocalMessage
    ) {
        val key = AesKeyGenerator.generateKey(pin.toString(), salt)

        val encryptedMessage = AesEncryptor.encryptMessage(message.text, key)

        val encryptedLocalMessage = EncryptedLocalMessage(
            messageId = message.messageId,
            conversationId = message.conversationId,
            encryptedText = encryptedMessage.first,
            sent = message.sent,
            iv = encryptedMessage.second
        )
        messageData.newMessage(encryptedLocalMessage)
    }


}