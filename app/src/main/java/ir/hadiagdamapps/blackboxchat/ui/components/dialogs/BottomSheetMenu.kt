package ir.hadiagdamapps.blackboxchat.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.hadiagdamapps.blackboxchat.data.models.MenuItem

@Composable
fun BottomSheetMenu(items: List<MenuItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        for (item in items)
        TextButton(onClick = item.onClick, modifier = Modifier.fillMaxWidth(), shape = RectangleShape) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = item.text, color = Color.White)
                Icon(painter = painterResource(id = item.icon), contentDescription = null, tint = Color.White)
            }
        }
    }
}
