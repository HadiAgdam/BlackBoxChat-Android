package ir.hadiagdamapps.blackboxchat.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import ir.hadiagdamapps.blackboxchat.data.LocalMessageHandler
import ir.hadiagdamapps.blackboxchat.data.OutgoingMessageHandler
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.message.LocalMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.PendingMessage
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ChatRoute

class ChatScreenViewmodel(
    context: Context,
    private val args: ChatRoute
) : ViewModel() {

    private val pin = Pin.fromHash(args.pin)!!
    private var localMessageHandler = LocalMessageHandler(
        context = context,
        conversationId = args.conversationId,
        pin = pin,
        salt = args.salt
    )
    private var outgoingMessageHandler = object :  OutgoingMessageHandler(
        context = context,
        conversationId = args.conversationId,
        pin = pin,
        salt = args.salt,
    ) {
        override fun messageSent(pendingMessageId: Long) {
            TODO("Not yet implemented")
        }
    }

    var screenTitle by mutableStateOf("")
        private set

    var localMessages = mutableStateListOf<LocalMessage>()
        private set

    var pendingMessages = mutableStateListOf<PendingMessage>()
        private set

    var scrollIndex: Int? by mutableStateOf(null)
        private set

    var chatBoxContent: String by mutableStateOf("")
        private set

    init {
        loadPendingMessages()
        loadLocalMessages()
    }

    //----------------------------------------------------------------------------------------------

    private fun loadPendingMessages() {
        outgoingMessageHandler.loadPendingMessages()
    }


    private fun loadLocalMessages() {
        localMessages = localMessageHandler.loadMessages() as SnapshotStateList<LocalMessage>
    }

    fun cancelSendingPendingMessageClick(pendingMessageId: Long) {
        // TODO
    }

    //----------------------------------------------------------------------------------------------

    fun chatBoxValueChange(newValue: String) {
        if (OutgoingMessageHandler.isValidTextMessage(newValue)) {
            chatBoxContent = newValue
        }
    }

    fun chatBoxSendClick() {
        outgoingMessageHandler.send(chatBoxContent)
    }

    //----------------------------------------------------------------------------------------------


}