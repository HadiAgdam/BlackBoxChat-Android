package ir.hadiagdamapps.blackboxchat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import ir.hadiagdamapps.blackboxchat.ui.components.ConversationItem
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.ConfirmDeleteDialog
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.InboxDetailsDialog
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.NewConversationDialog
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.PinDialog
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.ConversationsScreenViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(viewmodel: ConversationsScreenViewmodel) {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewmodel.showNewConversationDialog) {
        if (viewmodel.showNewConversationDialog)
            scope.launch { bottomSheetState.show() }
        else
            bottomSheetState.hide()
    }




    Screen(title = "Choose Conversation", fabClick = viewmodel::newConversationClick, snackBarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {

        LazyColumn {
            items(viewmodel.conversations) {
                ConversationItem(
                    label = it.label.display(),
                    detailsClick = { viewmodel.conversationDetailsClick(it.conversationId) },
                    hasUnseenMessage = it.hasNewMessage,
                    modifier = Modifier.clickable { viewmodel.conversationClick(it.conversationId) }
                )
            }
        }

        if (viewmodel.showDetailsDialog) InboxDetailsDialog(
            qrCode = viewmodel.detailsDialogQrCode,
            publicKey = viewmodel.detailsPublicKey,
            label = viewmodel.detailsDialogLabel,
            dismiss = viewmodel::detailsDismiss,
            copyPublicKey = viewmodel::detailsCopyPublicKey,
            deleteInbox = viewmodel::detailsDelete,
            labelValueChange = viewmodel::detailsLabelChange
        )

        if (viewmodel.showConfirmDeleteDialog) ConfirmDeleteDialog(
            yesClick = viewmodel::deleteDialogYesClick, dismiss = viewmodel::deleteDialogDismiss
        )

        if (viewmodel.showPinDialog) PinDialog(
            pin = viewmodel.pinDialogContent,
            onPinChanged = viewmodel::pinChanged,
            error = viewmodel.pinDialogError,
            dismiss = viewmodel::pinDialogDismiss,
            okClick = viewmodel::pinDialogSubmit
        )


        if (viewmodel.showNewConversationDialog)
            ModalBottomSheet(
                onDismissRequest = viewmodel::dismissDialogs,
                sheetState = bottomSheetState,
                dragHandle = null,
                shape = RectangleShape
            ) {

                if (viewmodel.showNewConversationDialog) NewConversationDialog(
                    fromClipboard = viewmodel::newConversationFromClipboard,
                    fromQrCode = viewmodel::newConversationFromQrCode
                )

            }

    }
}
