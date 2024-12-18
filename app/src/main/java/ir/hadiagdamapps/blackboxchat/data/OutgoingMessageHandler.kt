package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.pending_message.PendingMessageData
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.models.message.OutgoingMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.PendingMessage
import ir.hadiagdamapps.blackboxchat.data.network.MessageSender

abstract class OutgoingMessageHandler(
    private val context: Context,
    private val conversation: ConversationModel,
    private val pin: Pin,
    private val salt: String,
) {
    private val aesKey = AesKeyGenerator.generateKey(pin.toString(), salt)
    private val pendingMessages = ArrayList<OutgoingMessage>()
    private val data = PendingMessageData(context)
    private val inbox: InboxModel =
        InboxHandler(context).getInboxById(conversation.inboxId)!! // should
    private var sending: Boolean = false
    private val sender = MessageSender(
        context = context,
        conversationId = conversation.conversationId,
        pin = pin,
        salt = salt,
    )

    fun loadPendingMessages(): List<PendingMessage> =
        data.getByReceiver(conversation.publicKey).mapNotNull {
            PendingMessage(
                pendingMessageId = it.pendingMessageId,
                text = AesEncryptor.decryptMessage(
                    encryptedMessage = it.encryptedMessage,
                    aesKey = aesKey,
                    ivString = it.iv
                ) ?: return@mapNotNull null,
            )
        }

    init {
        startQueue()
    }

    fun send(text: String) {
        if (!isValidTextMessage(text)) throw Error.INVALID_OUTGOING_TEXT

        val aesKey = AesKeyGenerator.generateKey()
        val pKey = E2EKeyGenerator.getPublicKeyFromString(conversation.publicKey.display())

        val textMessage = """
            
            {
                "sender": "${inbox.inboxPublicKey}",
                 "message": "$text"
            }
            
        """.trimIndent()


        val encryptedMessage = AesEncryptor.encryptMessage(textMessage, aesKey)

        val message = OutgoingMessage(
            receiver = conversation.publicKey.display(),
            encryptionKey = E2EEncryptor.encryptAESKeyWithPublicKey(aesKey, pKey),
            encryptedMessage = encryptedMessage.first,
            iv = encryptedMessage.second
        )
        data.insert(message)
        pendingMessages.add(message)
    }

    private fun startQueue() {
        sending = true
        Thread {
            queueLoop()
        }.start()
    }

    private fun queueLoop() {
        while (sending) {
            if (pendingMessages.isEmpty()) Thread.sleep(1000)
            // attempt to send
            for (message in pendingMessages)
                try {
                    sender.send(message)
                    messageSent(message.pendingMessageId)
                    pendingMessages.remove(message)
                    data.deleteById(message.pendingMessageId)
                } catch (ex: Exception) {
                    Log.e("queue error", ex.toString())// TODO()
                }
        }
    }

    abstract fun messageSent(pendingMessageId: Long)

    companion object {
        fun isValidTextMessage(text: String): Boolean {
            return true // TODO
        }
    }
}