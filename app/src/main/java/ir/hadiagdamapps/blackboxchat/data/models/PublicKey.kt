package ir.hadiagdamapps.blackboxchat.data.models

class PublicKey private constructor(text: String){

    fun parse(text: String): PublicKey? {
        return if(isValid(text)) PublicKey(text) else null
    }

    private fun isValid(text: String): Boolean {
        TODO("validate the publicKey")
    }


}