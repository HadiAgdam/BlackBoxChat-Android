package ir.hadiagdamapps.blackboxchat.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import ir.hadiagdamapps.blackboxchat.ui.components.ConversationItem
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.ConfirmDeleteDialog
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.InboxDetailsDialog
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.PinDialog
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.ConversationsScreenViewmodel

@Composable
fun ConversationsScreen(viewmodel: ConversationsScreenViewmodel) {
    Screen(title = "Choose Conversation") {

        LazyColumn {
            items(viewmodel.conversations) {
                ConversationItem(
                    label = it.label.display(),
                    detailsClick = { viewmodel.conversationDetailsClick(it.conversationId) },
                    hasUnseenMessage = it.hasNewMessage
                )
            }
        }

        if (viewmodel.showDetailsDialog)
            InboxDetailsDialog(
                qrCode = viewmodel.detailsDialogQrCode,
                publicKey = viewmodel.detailsPublicKey,
                label = viewmodel.detailsDialogLabel,
                dismiss = viewmodel::detailsDismiss,
                copyPublicKey = viewmodel::detailsCopyPublicKey,
                deleteInbox = viewmodel::detailsDelete,
                labelValueChange = viewmodel::detailsLabelChange
            )

        if (viewmodel.showConfirmDeleteDialog)
            ConfirmDeleteDialog(
                yesClick = viewmodel::deleteDialogYesClick,
                dismiss = viewmodel::deleteDialogDismiss
            )

        if (viewmodel.showPinDialog)
            PinDialog(
                pin = viewmodel.pinDialogContent,
                onPinChanged = viewmodel::pinChanged,
                error = viewmodel.pinDialogError,
                dismiss = viewmodel::pinDialogDismiss,
                okClick = viewmodel::pinDialogSubmit)

    }
}
