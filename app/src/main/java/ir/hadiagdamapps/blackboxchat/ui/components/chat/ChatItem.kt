package ir.hadiagdamapps.blackboxchat.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.theme.ColorPalette
import ir.hadiagdamapps.blackboxchat.ui.theme.Typography

val boxPadding = 30.dp

@Composable
fun ChatItem(
    modifier: Modifier = Modifier, text: String, sent: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal =  12.dp, vertical = 4.dp)
            .padding(start = if (sent) boxPadding else 0.dp, end = if (sent) 0.dp else boxPadding),
        contentAlignment = if (sent) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .background(if (sent) ColorPalette.primary else ColorPalette.secondary)
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                if (sent) {
                    Spacer(modifier = Modifier.width(12.dp))
                    ChatTextContent(text = text)
                } else {
                    ChatTextContent(text = text)
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}

@Composable
fun ChatTextContent(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text, color = Color.White, style = Typography.bodyMedium, modifier = modifier
    )
}


@Composable
@Preview
private fun ChatItemPreview() {
    Screen(title = "Preview") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.background)
        ) {

            ChatItem(
                text = "0000000000000000000000000",
                sent = true
            )
        }
    }
}