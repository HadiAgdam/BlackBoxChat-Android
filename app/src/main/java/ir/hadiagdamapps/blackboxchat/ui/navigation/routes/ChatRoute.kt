package ir.hadiagdamapps.blackboxchat.ui.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoute(
    val conversationId: Long,
    val pin: String,
    val salt: String
)