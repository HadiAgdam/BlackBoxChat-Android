package ir.hadiagdamapps.blackboxchat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.components.dialogs.PinDialog
import ir.hadiagdamapps.blackboxchat.ui.components.inbox.InboxItem
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.InboxScreenViewmodel

@Composable
fun InboxesScreen(viewmodel: InboxScreenViewmodel) {

    Screen(
        title = "Choose Inbox",
        fabClick = viewmodel::newInbox
    ) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewmodel.inboxes) { inbox ->
                InboxItem(text = inbox.label.display(), modifier = Modifier.clickable {
                    viewmodel.inboxClick(inbox.inboxId)
                }) {
                    viewmodel.showInboxDialog(inbox)
                }
            }
        }


        if (viewmodel.showPinDialog)
            PinDialog(
                pin = viewmodel.pinDialogContent,
                onPinChanged = viewmodel::pinChanged,
                error = viewmodel.pinDialogError,
                dismiss = viewmodel::pinDialogDismiss,
                okClick = viewmodel::pinDialogSubmit)


    }

}