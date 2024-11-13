package ir.hadiagdamapps.blackboxchat.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ir.hadiagdamapps.blackboxchat.data.Clipboard
import ir.hadiagdamapps.blackboxchat.data.ConversationHandler
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel
import ir.hadiagdamapps.blackboxchat.data.qr.QrCodeGenerator
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ConversationsRoute

class ConversationsScreenViewmodel(
    context: Context,
    private val navController: NavController,
    private val args: ConversationsRoute
) : ViewModel() {

    private val conversationHandler = ConversationHandler(context)
    private val qrCodeGenerator = QrCodeGenerator()
    private val clipboard = Clipboard(context)

    private val _conversations = mutableStateListOf<ConversationModel>()
    val conversations: SnapshotStateList<ConversationModel> = _conversations

    private var selectedConversationId: Long = -1
    var showDetailsDialog by mutableStateOf(false)
        private set
    var detailsDialogQrCode: Bitmap? by mutableStateOf(null)
        private set
    var detailsPublicKey by mutableStateOf("")
        private set
    var detailsDialogLabel by mutableStateOf("")
        private set
    var showConfirmDeleteDialog by mutableStateOf(false)
        private set

    var showPinDialog by mutableStateOf(true)
        private set
    var pinDialogContent by mutableStateOf("")
        private set
    var pinDialogError by mutableStateOf("")
        private set

    //----------------------------------------------------------------------------------------------

    fun conversationDetailsClick(conversationId: Long) {
        _conversations.first { it.conversationId == conversationId }.apply {
            detailsDialogQrCode = qrCodeGenerator.generateCode(this.publicKey.display())
            detailsPublicKey = this.publicKey.display()
            detailsDialogLabel = this.label.display()
            selectedConversationId = this.conversationId
        }
        showDetailsDialog = true
    }

    fun detailsDismiss() {
        showDetailsDialog = false
    }

    fun detailsCopyPublicKey() {
        clipboard.copy(detailsPublicKey)
    }

    fun detailsDelete() {
        showConfirmDeleteDialog = true
        showDetailsDialog = false
    }

    fun detailsLabelChange(newLabel: String) {
        if (Label.isValid(newLabel)) detailsDialogLabel = newLabel
        _conversations.forEachIndexed { index, conversationModel ->
            if (conversationModel.conversationId == selectedConversationId)
                _conversations[index] = _conversations[index].copy(label = Label.create(newLabel)!!)
        }
    }

    fun deleteDialogDismiss() {
        showConfirmDeleteDialog = false
    }

    fun deleteDialogYesClick() {
        conversationHandler.delete(selectedConversationId)
        showConfirmDeleteDialog = false
        _conversations.removeIf { it.conversationId == selectedConversationId }
    }

    //----------------------------------------------------------------------------------------------

    fun pinChanged(newPin: String) {
        pinDialogError = ""
        if (newPin.isDigitsOnly() && newPin.length <= Pin.LENGTH) pinDialogContent = newPin
    }

    fun pinDialogDismiss() {
        navController.popBackStack()
    }

    fun pinDialogSubmit() {
        val pin = Pin.parse(pinDialogContent)

        if (pin == null) {
            pinDialogError = "invalid pin"
        } else {

            showPinDialog = false
            _conversations.clear()

            try {

                conversationHandler.loadConversations(
                    inboxId = args.inboxId,
                    pin = pin
                ).forEach {
                    _conversations.add(it)
                }

            } catch (ex: Exception) {
                navController.popBackStack()
            }

        }
    }

}