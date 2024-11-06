package ir.hadiagdamapps.blackboxchat.data.models

class PrivateKey private constructor(private val text: String) {

    companion object {

        fun parse(text: String): PrivateKey? {
            return PrivateKey(text)
        }

    }


    override fun toString(): String {
        TODO("return the encrypted display of private key")
    }

}