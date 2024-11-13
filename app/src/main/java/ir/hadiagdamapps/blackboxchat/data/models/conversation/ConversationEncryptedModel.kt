package ir.hadiagdamapps.blackboxchat.data.models.conversation

data class ConversationEncryptedModel(
    val conversationId: Long,
    val publicKeyEncrypted: String,
    val labelEncrypted: String,
    val hasNewMessage: Boolean,
    val publicKeyIv: String,
    val labelIv: String,
    val salt: String
)
