package ir.hadiagdamapps.blackboxchat.data.models

import androidx.core.text.isDigitsOnly

class Pin private constructor(private val text: String) {

    companion object {
        const val LENGTH = 6


        fun parse(text: String): Pin? {
            return if (text.isDigitsOnly() && text.length == LENGTH) Pin(text)
            else null
        }
    }

}