package ir.hadiagdamapps.blackboxchat.data.network

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.message.OutgoingMessage

class MessageSender(
    private val context: Context,
    private val conversationId: Long,
    private val pin: Pin,
    private val salt: String,
) {

    fun send(outgoingMessage: OutgoingMessage) {
        // TODO attempt to send message, if failed throw Exception
    }


}