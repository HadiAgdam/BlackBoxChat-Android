package ir.hadiagdamapps.blackboxchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ChatRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ConversationsRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.InboxesRoute
import ir.hadiagdamapps.blackboxchat.ui.screens.InboxesScreen
import ir.hadiagdamapps.blackboxchat.ui.theme.BlackBoxChatAndroidTheme
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.InboxScreenViewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackBoxChatAndroidTheme {


                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = InboxesRoute) {

                    composable<InboxesRoute> {

                        val viewmodel by remember {
                            mutableStateOf(InboxScreenViewmodel(this@MainActivity))
                        }

                        viewmodel.loadInboxes()

                        InboxesScreen(viewmodel = viewmodel)

                    }

                    composable<ConversationsRoute> { }

                    composable<ChatRoute> { }

                }


            }
        }
    }
}