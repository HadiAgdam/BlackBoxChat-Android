package ir.hadiagdamapps.blackboxchat.data.models.message

data class OutgoingMessage(
    val pendingMessageId: Long = -1,
    val receiver: String,
    val encryptionKey: String,
    val iv: String,
    val encryptedMessage: String
)

//{
//	"receiver": (receiver's public key),
//	"encryptionKey": (encrypted using E2EE),
//	"iv": (for AES encryption),
//	"encryptedMessage":
//	 (encrypted using AES) {
//		 "sender": (senders public key),
//		 "message": (message text),
//	},
//}