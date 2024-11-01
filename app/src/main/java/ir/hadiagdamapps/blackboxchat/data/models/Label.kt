package ir.hadiagdamapps.blackboxchat.data.models

class Label private constructor(private val text: String) {

    companion object {
        private const val MAX_LENGTH = 30
        private val INVALID_CHARS_REGEX = Regex("[^a-zA-Z0-9 ]") // adjust as needed

        fun create(text: String): Label? {
            return if (isValid(text)) Label(text) else null
        }

        fun isValid(text: String): Boolean {
            return text.length <= MAX_LENGTH && !text.contains(INVALID_CHARS_REGEX)
        }
    }

    fun display(): String =
        text

}