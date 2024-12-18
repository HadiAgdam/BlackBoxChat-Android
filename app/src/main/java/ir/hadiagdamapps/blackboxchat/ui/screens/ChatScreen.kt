package ir.hadiagdamapps.blackboxchat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import ir.hadiagdamapps.blackboxchat.ui.components.Screen
import ir.hadiagdamapps.blackboxchat.ui.components.chat.ChatBox
import ir.hadiagdamapps.blackboxchat.ui.components.chat.ChatItem
import ir.hadiagdamapps.blackboxchat.ui.components.chat.PendingChatItem
import ir.hadiagdamapps.blackboxchat.ui.theme.BlackBoxChatAndroidTheme
import ir.hadiagdamapps.blackboxchat.ui.viewmodels.ChatScreenViewmodel

@Composable
fun ChatScreen(viewmodel: ChatScreenViewmodel) {

    val state = rememberLazyListState()

    LaunchedEffect(viewmodel.scrollIndex) {
        if (viewmodel.scrollIndex != null) state.scrollToItem(viewmodel.scrollIndex!!)
    }

    BlackBoxChatAndroidTheme {
        Screen(
            title = viewmodel.screenTitle
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(state = state, modifier = Modifier.weight(1f)) {
                    items(viewmodel.localMessages) {
                        ChatItem(text = it.text, sent = it.sent)
                    }
                    items(viewmodel.pendingMessages) {
                        PendingChatItem(text = it.text) {
                            viewmodel.cancelSendingPendingMessageClick(
                                it.pendingMessageId
                            )
                        }
                    }
                }
                ChatBox(
                    text = viewmodel.chatBoxContent,
                    onValueChange = viewmodel::chatBoxValueChange,
                    sendClick = viewmodel::chatBoxSendClick
                )
            }
        }
    }
}
