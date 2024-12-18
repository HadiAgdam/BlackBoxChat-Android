package ir.hadiagdamapps.blackboxchat.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.R
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.theme.ColorPalette


@Composable
fun PendingChatItem(
    modifier: Modifier = Modifier, text: String, onCancelClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .padding(start = boxPadding, end = 0.dp), contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(ColorPalette.primary)
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCancelClick, modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = "cancel sending icon",
                        tint = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                ChatTextContent(text = text)
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Icon(
                    painter = painterResource(R.drawable.time_icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(16.dp),
                    tint = Color.DarkGray
                )
            }
        }
    }
}


@Preview
@Composable
private fun PendingChatItemPreview() {
    Screen(title = "Preview") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.background)
        ) {

            PendingChatItem(
                text = "000000000000000000000000000000000000000000000000000",
            ) {

            }
        }
    }
}