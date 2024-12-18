package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.message.MessageData
import ir.hadiagdamapps.blackboxchat.data.models.Pin
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

}