package ir.hadiagdamapps.blackboxchat.data

object Error {

    val INSERT_FAILED = Exception("Failed to insert to database")
    val PRIVATE_KEY_PARSE_ERROR = Exception("Failed to parse private key")
    val SAME_PUBLIC_KEY_CONVERSATION = Exception("Cannot start conversation with same public key")

}

