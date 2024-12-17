package ir.hadiagdamapps.blackboxchat.data.models.message

data class PendingMessage(
    val pendingMessageId: Long,
    val conversationId: Long,
    val text: String
)