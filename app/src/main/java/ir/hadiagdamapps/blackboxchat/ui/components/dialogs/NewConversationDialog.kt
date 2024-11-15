package ir.hadiagdamapps.blackboxchat.ui.components.dialogs

import androidx.compose.runtime.Composable
import ir.hadiagdamapps.blackboxchat.R
import ir.hadiagdamapps.blackboxchat.data.models.MenuItem

@Composable
fun NewConversationDialog(fromClipboard: () -> Unit, fromQrCode: () -> Unit) {

    BottomSheetMenu(
        items = listOf(
            MenuItem("Get from clipboard", R.drawable.copy_icon, fromClipboard),
            MenuItem("Scan QrCode", R.drawable.scan_qr_code_icon, fromQrCode)
        )
    )
}