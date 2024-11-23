package ir.hadiagdamapps.blackboxchat.data.models.message

import ir.hadiagdamapps.blackboxchat.data.Error
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import org.json.JSONObject

data class MessageContent(
    val senderPublicKey: PublicKey,
    val text: String
) {


    companion object {
        fun fromJsonString(jsonString: String): MessageContent {
            JSONObject(jsonString).apply {
                return MessageContent(
                    senderPublicKey = PublicKey.parse(getString("sender_public_key")) ?: throw Error.INVALID_PUBLIC_KEY,
                    text = getString("text")
                )
            }
        }
    }

}
