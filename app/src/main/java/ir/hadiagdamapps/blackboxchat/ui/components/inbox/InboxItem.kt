package ir.hadiagdamapps.blackboxchat.ui.components.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.R
import ir.hadiagdamapps.blackboxchat.ui.theme.ColorPalette
import ir.hadiagdamapps.blackboxchat.ui.theme.Typography


@Composable
fun InboxItem(
    modifier: Modifier = Modifier,
    text: String,
    moreClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(ColorPalette.background)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            text = text,
            style = Typography.titleMedium,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.weight(.9f),
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(.05f))

        IconButton(onClick = moreClick, modifier = Modifier.fillMaxWidth(.1f)) {
            Icon(
                painter = painterResource(id = R.drawable.more_horizontal_icon), contentDescription = "more",
                modifier = Modifier.fillMaxSize(),
                tint = Color.White
            )
        }

    }

}

