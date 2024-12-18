package ir.hadiagdamapps.blackboxchat.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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
import ir.hadiagdamapps.blackboxchat.data.InboxHandler
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesEncryptor
import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes.AesKeyGenerator
import ir.hadiagdamapps.blackboxchat.data.models.ConnectionStatus
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PrivateKey
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.models.message.LocalMessage
import ir.hadiagdamapps.blackboxchat.data.network.MessageReceiver
import ir.hadiagdamapps.blackboxchat.data.qr.QrCodeGenerator
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ChatRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ConversationsRoute

class ConversationsScreenViewmodel(
    context: Context, private val navController: NavController, private val args: ConversationsRoute
) : ViewModel() {

    private var pin: Pin? = null
    private val conversationHandler = ConversationHandler(context)
    private val qrCodeGenerator = QrCodeGenerator()
    private val clipboard = Clipboard(context)
    private var inbox: InboxModel = InboxHandler(context).getInboxById(args.inboxId).let {
        if (it == null) {
            navController.popBackStack()
        }
        it!! // unreachable
    }
    private val messageReceiver = object : MessageReceiver(
        context = context,
        inboxId = inbox.inboxId,
        salt = inbox.salt
    ) {
        override fun newConversation(conversationModel: ConversationModel) {
            _conversations.add(conversationModel)
        }

        override fun onReceived(message: LocalMessage) {
            _conversations.indexOfFirst { it.conversationId == message.conversationId }
                .takeIf { it != -1 }?.let {
                    _conversations[it] = _conversations[it].copy(hasNewMessage = true)
                    conversationHandler.updateHasNewMessageStatus(conversations[it].conversationId, true)
                }
        }

        override fun connectionStatusChanged(connectionStatus: ConnectionStatus) {
            // TODO("Not yet implemented")
        }
    }


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

    var showNewConversationDialog by mutableStateOf(false)
        private set

    var showInvalidClipboard by mutableStateOf(false)
        private set

    //----------------------------------------------------------------------------------------------

    fun conversationClick(conversationId: Long) {
        navController.navigate(ChatRoute(conversationId, pin.toString(), inbox.salt))
        conversationHandler.updateHasNewMessageStatus(conversationId, false)
    }

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
            if (conversationModel.conversationId == selectedConversationId) _conversations[index] =
                _conversations[index].copy(label = Label.create(newLabel)!!).apply {
                    conversationHandler.updateLabel(label, pin!!, conversationId) // not sure
                }
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
        pin = Pin.parse(pinDialogContent)
        pinDialogContent = ""

        if (pin == null) {
            pinDialogError = "invalid pin"
        } else {

            showPinDialog = false
            _conversations.clear()

            try {

                inbox = inbox.copy(inboxPrivateKey = PrivateKey.parse(AesEncryptor.decryptMessage(
                    inbox.inboxPrivateKey.toString(), AesKeyGenerator.generateKey(
                        pin.toString(), inbox.salt
                    ), inbox.iv
                ).apply { Log.e("decrypted private key", this.toString()) }
                    ?: throw Exception("null decrypted message"))!!)

                conversationHandler.loadConversations(
                    inboxId = args.inboxId, pin = pin!!
                ).forEach {
                    _conversations.add(it)
                }

                messageReceiver.init(inbox.inboxPrivateKey, pin!!)
                startPolling()

            } catch (ex: Exception) {
                Log.e("backstack", ex.toString())
                navController.popBackStack()
            }

        }
    }


    //----------------------------------------------------------------------------------------------

    fun newConversationClick() {
        showNewConversationDialog = true
    }

    fun dismissDialogs() {
        showNewConversationDialog = false
    }

    fun newConversationFromClipboard() {
        showNewConversationDialog = false

        try {
            clipboard.readClipboard()?.let {
                PublicKey.parse(it)
            }?.apply {
                _conversations.add(
                    conversationHandler.newConversation(this, pin!!, args.inboxId)
                )
            } ?: run {
                showInvalidClipboard = true
            }
        } catch (ex: Exception) {
            showInvalidClipboard = true
        }
    }

    fun newConversationFromQrCode() {
        showNewConversationDialog = false
        // TODO
    }


    //----------------------------------------------------------------------------------------------

    private fun startPolling() {
        messageReceiver.startPolling()
    }

}