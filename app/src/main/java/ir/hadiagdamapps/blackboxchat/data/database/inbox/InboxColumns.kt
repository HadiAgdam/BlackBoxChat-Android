package ir.hadiagdamapps.blackboxchat.data.database.inbox

import ir.hadiagdamapps.blackboxchat.data.database.Column

enum class InboxColumns(override val definition: String) : Column {



    INBOX_ID("INTEGER PRIMARY KEY AUTOINCREMENT"),
    INBOX_PUBLIC_KEY("TEXT NOT NULL UNIQUE"),
    INBOX_PRIVATE_KEY("TEXT NOT NULL"),
    LABEL("TEXT"),
    HAS_NEW_MESSAGE("BOOLEAN"),
    IV("TEXT"),
    SALT("TEXT"),
    LAST_MESSAGE_ID("INTEGER DEFAULT 0"),
    ;

    override val columnName: String = name.lowercase()

    override fun toString(): String = name.lowercase()
}