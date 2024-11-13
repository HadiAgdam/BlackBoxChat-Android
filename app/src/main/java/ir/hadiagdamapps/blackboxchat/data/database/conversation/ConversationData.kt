package ir.hadiagdamapps.blackboxchat.data.database.conversation

import android.content.Context
import androidx.compose.animation.slideInVertically
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationModel
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationColumns.*
import ir.hadiagdamapps.blackboxchat.data.database.generateWhereQuery
import ir.hadiagdamapps.blackboxchat.data.database.getBoolean
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.conversation.ConversationEncryptedModel

class ConversationData(context: Context) : DatabaseHelper(context, Table.CONVERSATIONS) {

    fun getConversations(
        where: HashMap<ConversationColumns, String>? = null
    ): List<ConversationEncryptedModel> {
        val c = readableDatabase.rawQuery(
            """
                SELECT
                    $CONVERSATION_ID,
                    $PUBLIC_KEY,
                    $LABEL,
                    $HAS_NEW_MESSAGE,
                    $PUBLIC_KEY,
                    $LABEL_IV,
                    $SALT
                from ${table.tableName}
                ${generateWhereQuery(where)}
                
                
            """.trimIndent(),
            where?.values?.toTypedArray()
        )

        return ArrayList<ConversationEncryptedModel>().apply {

            if (c.moveToFirst()) do
                add(
                    ConversationEncryptedModel(
                        conversationId = c.getLong(0),
                        publicKeyEncrypted = c.getString(1),
                        labelEncrypted = c.getString(2),
                        hasNewMessage = c.getBoolean(3),
                        publicKeyIv = c.getString(4),
                        labelIv = c.getString(5),
                        salt = c.getString(6)
                    )
                )
            while (c.moveToNext())

            close()
        }
    }

    private fun delete(where: HashMap<ConversationColumns, String>) {
        writableDatabase.delete(table.tableName, where.keys.joinToString(" AND ") { "${it.columnName} = ?" }, where.values.toTypedArray())
    }

    fun delete(conversationId: Long) {
        delete(where = hashMapOf(CONVERSATION_ID to conversationId.toString()))
    }

}