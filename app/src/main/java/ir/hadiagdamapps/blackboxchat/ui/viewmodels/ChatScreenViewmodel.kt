package ir.hadiagdamapps.blackboxchat.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ir.hadiagdamapps.blackboxchat.data.LocalMessageHandler
import ir.hadiagdamapps.blackboxchat.data.models.message.LocalMessage
import ir.hadiagdamapps.blackboxchat.data.models.message.PendingMessage

class ChatScreenViewmodel(context: Context) : ViewModel() {

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
        // TODO
    }


    private fun loadLocalMessages() {
        // TODO
    }

    fun cancelSendingPendingMessageClick(pendingMessageId: Long) {
        // TODO
    }

    //----------------------------------------------------------------------------------------------

    fun chatBoxValueChange(newValue: String) {
        // TODO
    }

    fun chatBoxSendClick() {
        // TODO
    }

    //----------------------------------------------------------------------------------------------


}