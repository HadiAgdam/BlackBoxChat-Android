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
import androidx.compose.ui.platform.LocalContext

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

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )

}