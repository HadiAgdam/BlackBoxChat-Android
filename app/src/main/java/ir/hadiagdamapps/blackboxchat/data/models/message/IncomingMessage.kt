package ir.hadiagdamapps.blackboxchat.data.models.message

import org.json.JSONObject

data class IncomingMessage(
    val messageId: Long,
    val encryptionKey: String, // should be decrypted using private key
    val encryptedMessage: String, // encrypted using AES with private key as aes key
    val iv: String
) {

    companion object {
        fun fromJson(json: JSONObject): IncomingMessage  {
            return IncomingMessage(
                messageId = json.getLong("messageId"),
                encryptionKey = json.getString("encryptionKey"),
                encryptedMessage = json.getString("encryptedMessage"),
                iv = json.getString("messageIv")
            )
        }
    }

}
