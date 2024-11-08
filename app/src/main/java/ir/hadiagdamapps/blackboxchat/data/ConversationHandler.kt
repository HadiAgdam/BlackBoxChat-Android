package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel

class ConversationHandler(
    private val context: Context
) {

    fun loadConversations(inboxId: Long, pin: Pin): List<ConversationModel> {
        return listOf(
            ConversationModel(
                0,
                PublicKey.parse("public key")!!,
                Label.create("label")!!,
                true
            )
        )
        TODO("load the conversations from database and decrypt them using pin" +
                "if pin was not valid, throw exception")
    }

    fun delete(inboxId: Long) {
        Log.e("deleted", inboxId.toString())
    }



}