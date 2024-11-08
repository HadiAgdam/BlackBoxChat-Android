package ir.hadiagdamapps.blackboxchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.ui.theme.Typography

@Composable
fun UnseenMessageCountBadge(
    modifier: Modifier = Modifier,
    count: Int? = null
) {

    Box(modifier = modifier
        .size(24.dp)
        .clip(CircleShape.copy(CornerSize(100)))
        .background(Color.White),
        contentAlignment = Alignment.Center
        ) {
        Text(text = count?.toString() ?: "", color = Color.Black, style = Typography.labelMedium)
    }


}
