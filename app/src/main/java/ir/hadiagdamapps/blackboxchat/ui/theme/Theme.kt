package ir.hadiagdamapps.blackboxchat.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val ColorScheme = lightColorScheme(
    primary = ColorPalette.primary,
    secondary = ColorPalette.secondary,
    background = ColorPalette.background,
    surface = ColorPalette.background,
)

@Composable
fun BlackBoxChatAndroidTheme(
    content: @Composable () -> Unit
) {

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Black, // Background color
            darkIcons = false // Set to false for white text, true for black text
        )
        systemUiController.setNavigationBarColor(
            color = Color.Black,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )

}