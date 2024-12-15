package ir.hadiagdamapps.blackboxchat.data.database.conversation

import ir.hadiagdamapps.blackboxchat.data.database.Column

enum class ConversationColumns (override val definition: String): Column {

    CONVERSATION_ID("INTEGER PRIMARY KEY AUTOINCREMENT"),
    INBOX_ID("INTEGER"),
    PUBLIC_KEY("TEXT"), // encrypted
    LABEL("TEXT"), // encrypted
    HAS_NEW_MESSAGE("BOOLEAN DEFAULT TRUE"),
    PUBLIC_KEY_IV("TEXT"), // for encryption
    LABEL_IV("TEXT"),
    SALT("TEXT") // for encryption
    ;

    override val columnName: String = name.lowercase()

    override fun toString(): String = name.lowercase()
}