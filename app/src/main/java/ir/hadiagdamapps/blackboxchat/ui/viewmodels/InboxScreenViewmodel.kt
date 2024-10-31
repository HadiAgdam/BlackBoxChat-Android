package ir.hadiagdamapps.blackboxchat.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import ir.hadiagdamapps.blackboxchat.data.InboxHandler
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxPreviewModel

class InboxScreenViewmodel(context: Context) : ViewModel() {

    private val inboxHandler = InboxHandler(context)

    private val _inboxes = mutableStateListOf<InboxPreviewModel>()
    val inboxes: SnapshotStateList<InboxPreviewModel> = _inboxes

    var showInboxDialog by mutableStateOf(false)
        private set
    var inboxDialogContent: InboxPreviewModel? = null
        private set
    var inboxDialogLabel: Label? = null
        private set


    var showPinDialog by mutableStateOf(false)
        private set
    var pinDialogContent by mutableStateOf("")
        private set
    var pinDialogError by mutableStateOf("")
        private set


    fun pinDialogDismiss() {
        pinDialogContent = ""
        showPinDialog = false
    }

    fun pinDialogSubmit() {
        val pin = Pin.parse(pinDialogContent)

        if (pin != null) {
            _inboxes.add(
                inboxHandler.createInbox(pin)
            )
            pinDialogDismiss()
        } else
            pinDialogError = "Invalid pin format"

    }

    fun pinChanged(newPin: String) {
        if (newPin.length <= Pin.LENGTH) {
            pinDialogContent = newPin
            pinDialogError = ""
        }
    }

    fun newInbox() {
        pinDialogContent = ""
        pinDialogError = ""
        showPinDialog = true
    }


    // ---------------------------------------------------------------------------------------------

    fun loadInboxes() {
        _inboxes.clear()
        inboxHandler.getInboxes().forEach {
            _inboxes.add(it)
        }
    }

    fun inboxClick(inboxId: Long) {
        TODO("Open the conversations screen and pass InboxId")
    }

    fun showInboxDialog(inbox: InboxPreviewModel) {
        TODO(
            "set the content of inboxDialog" +
                    "set showInbox to true"
        )
    }

    // ---------------------------------------------------------------------------------------------

}