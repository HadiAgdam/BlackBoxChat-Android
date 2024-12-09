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
import org.json.JSONArray

abstract class MessageReceiver(
    context: Context,
    private val inboxId: Long,
    private val inboxPrivateKey: PrivateKey,
    private var inboxPin: Pin?,
    salt: String

) {

    fun setPin(pin: Pin) {
        inboxPin = pin
    }

    private var isLooping = false
    private val queue = MessengerApp.queue!!
    private val conversationHandler = ConversationHandler(context)
    private val inboxData = InboxData(context)
    private val messageData = MessageData(context)
    private val aesKey = AesKeyGenerator.generateKey(inboxPin.toString(), salt)
    private val baseUrl =
        "https://hadiagdam0.pythonanywhere.com/api" // I know it should not be hardcoded

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

        val conversation = conversationHandler.getConversationByPublicKey(content.senderPublicKey)
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
        queue.add(
            StringRequest(Request.Method.GET, Uri.parse("$baseUrl/get_message").buildUpon()?.apply {
                appendQueryParameter("publicKey", inboxModel.inboxPublicKey.display())
                appendQueryParameter("messageId", inboxModel.lastMessageId.toString())
            }?.build().toString().apply {
                Log.e("request", this)
            }, { response: String ->
                Log.e("receiver volley response", response)
                JSONArray(response).apply {
                    for (i in 0 until length()) newMessage(IncomingMessage.fromJson(getJSONObject(i)))
                }
            }, failed)
        )
    }

    private suspend fun loop() {
        while (isLooping) {
            try {

                poll(newMessage = {
                    connectionStatusChanged(ConnectionStatus.CONNECTED)
                    handleIncomingMessage(it)
                }, failed = {
                    throw it
                })

            } catch (ex: Exception) {
                connectionStatusChanged(ConnectionStatus.DISCONNECTED)
            }
            kotlinx.coroutines.delay(5000)
        }
    }

    suspend fun startPolling() {
        isLooping = true
        loop()
    }

    fun stopPolling() {
        isLooping = false
    }

}