package ir.hadiagdamapps.blackboxchat.data.models.message

data class EncryptedLocalMessage(
    val messageId: Long,
    val conversationId: Long,
    val encryptedText: String,
    val sent: Boolean,
    val iv: String
)