package ir.hadiagdamapps.blackboxchat.data.network

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import ir.hadiagdamapps.blackboxchat.MessengerApp
import ir.hadiagdamapps.blackboxchat.data.ConversationHandler
import ir.hadiagdamapps.blackboxchat.data.Error
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxData
import ir.hadiagdamapps.blackboxchat.data.database.message.MessageData
import ir.hadiagdamapps.blackboxchat.data.models.ConnectionStatus
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PrivateKey
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel
import ir.hadiagdamapps.blackboxchat.data.models.message.EncryptedLocalMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.IncomingMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.LocalMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.MessageContent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

abstract class MessageReceiver(
    context: Context,
    private val inboxId: Long,
    salt: String

) {

    private var inboxPrivateKey: PrivateKey? = null
    private var inboxPin: Pin? = null

    fun init(inboxPrivateKey: PrivateKey, inboxPin: Pin) { // WTF ?
        this.inboxPrivateKey = inboxPrivateKey
        this.inboxPin = inboxPin
    }

    private val mockPublicKey =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE9WpEM40cKejzcg2aHWjcCnHMqVJYf05CT8qq+v0AbICyVNtEOW/bPEVnPGuezUu1Y1YIYDlliEfzgYFGGDRZPQ=="
    private val sdf = SimpleDateFormat("dd hh:mm ss", Locale.getDefault())
    private var isLooping: Boolean = false
    private val conversationHandler = ConversationHandler(context)
    private val inboxData = InboxData(context)
    private val messageData = MessageData(context)
    private val aesKey = AesKeyGenerator.generateKey(inboxPin.toString(), salt)

    private var inboxModel =
        inboxData.getInboxes(hashMapOf(InboxColumns.INBOX_ID to inboxId.toString()))
            .first { it.inboxId == inboxId }


    abstract fun newConversation(conversationModel: ConversationModel)

    abstract fun onReceived(message: LocalMessage)

    abstract fun connectionStatusChanged(connectionStatus: ConnectionStatus)

    private fun handleIncomingMessage(message: IncomingMessage) {

        val key = E2EEncryptor.decryptAESKeyWithPrivateKey(
            encryptedAESKey = message.encryptionKey,
            privateKey = E2EKeyGenerator.getPrivateKeyFromString(inboxPrivateKey.toString())
        )

        val content = MessageContent.fromJsonString(
            AesEncryptor.decryptMessage(message.encryptedMessage, key, message.iv)
                ?: throw Error.INVALID_JSON
        )

        val conversation =
            conversationHandler.getConversationByPublicKey(content.senderPublicKey, inboxPin!!)
                ?: conversationHandler.newConversation(
                    publicKey = content.senderPublicKey, pin = inboxPin!!, inboxId = inboxId
                ).apply {
                    newConversation(this)
                }


        val encryptedText = AesEncryptor.encryptMessage(content.text, aesKey)


        val encryptedLocalMessage = EncryptedLocalMessage(
            messageId = message.messageId,
            conversationId = conversation.conversationId,
            encryptedText = encryptedText.first,
            sent = false,
            iv = encryptedText.second
        )

        messageData.newMessage(encryptedLocalMessage)



        onReceived(
            LocalMessage(
                messageId = message.messageId,
                conversationId = conversation.conversationId,
                text = content.text,
                sent = false
            )
        )

        if (message.messageId > inboxModel.lastMessageId) {
            inboxModel = inboxModel.copy(lastMessageId = message.messageId)
            inboxData.updateLastMessageId(inboxModel.inboxId, message.messageId)
        }
    }

    private fun poll(newMessage: (IncomingMessage) -> Unit, failed: (VolleyError) -> Unit) {

        Random().apply {
            if (nextBoolean() && nextBoolean()) { // 1/4 chance
                failed(VolleyError())
                return
            }
        }

        val aesKey = AesKeyGenerator.generateKey()
        val encryptionKey = E2EEncryptor.encryptAESKeyWithPublicKey(
            aesKey, E2EKeyGenerator.getPublicKeyFromString(inboxModel.inboxPublicKey.display())
        )


        val encryptedMessage = AesEncryptor.encryptMessage(
            """
            {
                "sender_public_key": "$mockPublicKey",
                "text": "Hello !${sdf.format(Date())}" 
            }
        """.trimIndent(), aesKey
        )


        val message = IncomingMessage(
            messageId = inboxModel.lastMessageId,
            encryptionKey = encryptionKey,
            encryptedMessage = encryptedMessage.first,
            iv = encryptedMessage.second
        )
        newMessage(message)
    }

    private fun loop() {
        while (isLooping) {
            try {

                poll(newMessage = {
                    connectionStatusChanged(ConnectionStatus.CONNECTED)
                    handleIncomingMessage(it)
                }, failed = {
                    // TODO handle
                })

            } catch (ex: Exception) {
                connectionStatusChanged(ConnectionStatus.DISCONNECTED)
            }
            // temp
            Thread.sleep(3000)
        }
    }

    fun startPolling() {
        isLooping = true
        Thread {
            loop()
        }.start()
    }

    fun stopPolling() {
        isLooping = false
    }

}