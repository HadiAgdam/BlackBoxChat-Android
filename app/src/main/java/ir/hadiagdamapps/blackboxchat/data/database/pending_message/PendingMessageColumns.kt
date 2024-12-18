package ir.hadiagdamapps.blackboxchat.data.database.pending_message

import ir.hadiagdamapps.blackboxchat.data.database.Column

enum class PendingMessageColumns(override val definition: String) : Column {

    MESSAGE_ID("INTEGER PRIMARY KEY AUTOINCREMENT"),
    RECEIVER("TEXT"),
    ENCRYPTION_KEY("TEXT"),
    IV("TEXT"),
    ENCRYPTED_MESSAGE("TEXT")
    ;

    override val columnName: String
        get() = name.lowercase()

    override fun toString() = name.lowercase()


}