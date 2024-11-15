package ir.hadiagdamapps.blackboxchat.data.models

import android.util.Log
import androidx.core.util.toHalf


class Label private constructor(private val text: String) {

    companion object {

        fun create(publicKey: PublicKey): Label {
            return Label(publicKey.display().substring(32, publicKey.display().length - 1)) // temp
        }

        fun create(text: String): Label? {
            return if (isValid(text)) Label(text) else null
        }

        fun isValid(text: String): Boolean {
            return true
        }
    }

    fun display(): String =
        text

}