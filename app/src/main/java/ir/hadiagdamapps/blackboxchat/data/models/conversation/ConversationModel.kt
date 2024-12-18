package ir.hadiagdamapps.blackboxchat.data.models.conversation

import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey

data class ConversationModel(
    val conversationId: Long,
    val publicKey: PublicKey,
    val label: Label,
    val hasNewMessage: Boolean,
    val inboxId: Long
)