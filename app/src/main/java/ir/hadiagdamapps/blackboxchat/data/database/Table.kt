package ir.hadiagdamapps.blackboxchat.data.database

import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationColumns
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns
import ir.hadiagdamapps.blackboxchat.data.database.message.MessageColumns
import ir.hadiagdamapps.blackboxchat.data.database.pending_message.PendingMessageColumns
import kotlin.enums.EnumEntries

enum class Table
    (
    val tableName: String, val columnsAsQuery: String
) {

    INBOXES(
        tableName = "inboxes",
        columnsAsQuery = InboxColumns.entries.toTypedArray().asQuery()
    ),

    CONVERSATIONS(
        tableName = "conversations",
        columnsAsQuery = ConversationColumns.entries.toTypedArray().asQuery()
    ),

    MESSAGES(
        tableName = "messages",
        columnsAsQuery = MessageColumns.entries.toTypedArray().asQuery()
    ),
    PENDING_MESSAGES(
        tableName = "pending_messages",
        columnsAsQuery = PendingMessageColumns.entries.toTypedArray().asQuery()
    )

    ;

    val createQuery = "CREATE TABLE $tableName ($columnsAsQuery) "
    val dropQuery = "DROP TABLE IF EXISTS $tableName";

}


inline fun <reified T> Array<out T>.asQuery(): String where T : Enum<T>, T : Column {
    return this.joinToString(separator = ",\n") { "${it.columnName} ${it.definition}" }
}

fun  <T: HashMap<ConversationColumns, String>> T.asWhereQuery() : String {
    return this.keys.joinToString(separator = "AND") { "${it.columnName} = ?" }
}