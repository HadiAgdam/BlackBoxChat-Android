package ir.hadiagdamapps.blackboxchat.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ChatRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.ConversationsRoute
import ir.hadiagdamapps.blackboxchat.ui.navigation.routes.InboxesRoute
import ir.hadiagdamapps.blackboxchat.ui.screens.ChatScreen
import ir.hadiagdamapps.blackboxchat.ui.screens.ConversationsScreen
import ir.hadiagdamapps.blackboxchat.ui.screens.InboxesScreen
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.ChatScreenViewmodel
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.ConversationsScreenViewmodel
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.InboxScreenViewmodel

@Composable
fun AppNavHost(navController: NavHostController, context: Context) {


    NavHost(navController = navController, startDestination = InboxesRoute) {


        composable<InboxesRoute> {

            val viewmodel by remember {
                mutableStateOf(
                    InboxScreenViewmodel(
                        context = context,
                        navController = navController
                    )
                )
            }

            InboxesScreen(viewmodel = viewmodel)

        }

        composable<ConversationsRoute> {

            val args = it.toRoute<ConversationsRoute>()

            val viewmodel by remember {
                mutableStateOf(ConversationsScreenViewmodel(
                    context = context,
                    navController=navController,
                    args=args
                ))
            }

            ConversationsScreen(viewmodel = viewmodel)

        }

        composable<ChatRoute> {
            val args = it.toRoute<ChatRoute>()

            val viewmodel by remember {
                mutableStateOf(ChatScreenViewmodel(context, args))
            }
            ChatScreen(viewmodel)
        }

    }


}