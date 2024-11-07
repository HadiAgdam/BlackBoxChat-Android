package ir.hadiagdamapps.blackboxchat.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.theme.BlackBoxChatAndroidTheme
import ir.hadiagdamapps.blackboxchat.ui.theme.Typography
import ir.hadiagdamapps.e2eemessenger.ui.components.widgets.TextButton

@Composable
fun ConfirmDeleteDialog(
    yesClick: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        shape = RectangleShape,
        onDismissRequest = dismiss, confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(text = "no", onClick = dismiss)

                TextButton(text = "yes", onClick = yesClick)

            }
        }, text = {
            Column {
                Text(
                    text = "Delete",
                    style = Typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Are you sure you want do Delete ?",
                    style = Typography.bodyMedium,
                    color = Color.White
                )
            }
        })
}


@Preview
@Composable
fun ConfirmDeleteDialogPreview() {
    BlackBoxChatAndroidTheme {
        Screen(title = "Preview") {
            ConfirmDeleteDialog(yesClick = { /*TODO*/ }) {

            }
        }
    }
}

