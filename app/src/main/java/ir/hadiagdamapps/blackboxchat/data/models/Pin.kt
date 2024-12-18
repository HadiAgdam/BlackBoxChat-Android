package ir.hadiagdamapps.blackboxchat.data.models

import androidx.core.text.isDigitsOnly
import ir.hadiagdamapps.blackboxchat.data.crypto.hashing.sha256.Sha256

class Pin private constructor(private val text: String) {

    companion object {
        const val LENGTH = 6


        fun parse(text: String): Pin? {
            return if (isValid(text)) Pin(
                Sha256.hash(text)
            )
            else null
        }

        fun fromHash(hash: String): Pin? {
            return Pin(hash) // not complete
        }

        private fun isValid(text: String): Boolean = text.isDigitsOnly() && text.length == LENGTH
    }


    override fun toString() = text

}