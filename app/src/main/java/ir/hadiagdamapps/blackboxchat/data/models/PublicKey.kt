package ir.hadiagdamapps.blackboxchat.data.models

import ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e.E2EKeyGenerator.toText

class PublicKey private constructor(private val text: String) {

    companion object {

        fun parse(publicKey: java.security.PublicKey): PublicKey {
            return PublicKey(publicKey.toText())
        }

        fun parse(text: String): PublicKey? {
            return if (isValid(text)) PublicKey(text) else null
        }

        private fun isValid(text: String): Boolean {
            //TODO("validate the publicKey")
            return true
        }
    }


    fun display(): String = text

}