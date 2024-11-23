package ir.hadiagdamapps.blackboxchat.data.models.message

data class LocalMessage (
    val messageId: Long,
    val conversationId: Long,
    val text: String,
    val sent: Boolean,
)