package ir.hadiagdamapps.blackboxchat.ui.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
data class ConversationsRoute(
    private val inboxId: Long
)