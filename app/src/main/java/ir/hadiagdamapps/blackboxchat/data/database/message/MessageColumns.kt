package ir.hadiagdamapps.blackboxchat.data.database.message

import ir.hadiagdamapps.blackboxchat.data.database.Column

enum class MessageColumns(override val definition: String) : Column {

    MESSAGE_ID("INTEGER PRIMARY KEY NOT NULL UNIQUE"),
    CONVERSATION_ID("INTEGER NOT NULL"),
    TEXT("TEXT NOT NULL"),
    SENT("BOOLEAN"),
    IV("TEXT")
    ;

    override val columnName: String = name.lowercase()

    override fun toString(): String = name.lowercase()
}