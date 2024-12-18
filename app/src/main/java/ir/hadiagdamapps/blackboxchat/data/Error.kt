package ir.hadiagdamapps.blackboxchat.data

object Error {

    val PRIVATE_KEY_PARSE_ERROR = Exception("Failed to parse private key")
    val INVALID_JSON = Exception("Invalid json content")
    val INVALID_PUBLIC_KEY = Exception("Invalid public key")
    val INVALID_OUTGOING_TEXT = Exception("Invalid text for outgoing message")

    val INSERT_FAILED = Exception("Failed to insert to database")

    val NOT_UNIQUE_MESSAGE_ID = Exception("MessageId is should be unique.")

    val NOT_UNIQUE_PUBLIC_KEY_CONVERSATION = Exception("PublicKey should be unique")
    val SAME_PUBLIC_KEY_CONVERSATION = Exception("Cannot start conversation with same public key")
    val CONVERSATION_ID_NOT_FOUND = Exception("Could not find conversation with conversationId")

}

