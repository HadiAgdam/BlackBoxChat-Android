package ir.hadiagdamapps.blackboxchat.data.database

import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns
import kotlin.enums.EnumEntries

enum class Table
    (
    val tableName: String,
    val createQuery: String,
    val dropQuery: String
) {

    INBOXES(
        "inboxes",
        """
                CREATE TABLE inboxes (
                
                ${InboxColumns.entries.toTypedArray().asQuery()}
                
                )
            """.trimIndent(),
        "DROP TABLE IF EXISTS inboxes"
    );
}


inline fun <reified T> Array<out T>.asQuery(): String where T : Enum<T>, T : Column {
    return this.joinToString(separator = ",\n") { "${it.columnName} ${it.definition}" }
}
