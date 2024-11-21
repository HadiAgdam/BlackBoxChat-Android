package ir.hadiagdamapps.blackboxchat.data.database.conversation

import android.content.ContentValues
import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.database.conversation.ConversationColumns.*
import ir.hadiagdamapps.blackboxchat.data.database.generateWhereQuery
import ir.hadiagdamapps.blackboxchat.data.database.getBoolean
import ir.hadiagdamapps.blackboxchat.data.database.put
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
                    $PUBLIC_KEY_IV,
                    $LABEL_IV,
                    $SALT
                from ${table.tableName}
                ${generateWhereQuery(where)}
                
                
            """.trimIndent().apply { Log.e("query", this) },
            where?.values?.toTypedArray()
        )



        Log.e("size", c.count.toString())

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
                    ).apply {
                        Log.e("id", this.conversationId.toString())
                        Log.e("public key", this.publicKeyEncrypted)
                        Log.e("label", this.labelEncrypted)
                        Log.e("has new message", this.hasNewMessage.toString())
                        Log.e("p iv", this.publicKeyIv)
                        Log.e("l iv", this.labelIv)
                        Log.e("salt", this.salt)
                    }
                )
            while (c.moveToNext())

            close()
        }
    }

    private fun delete(where: HashMap<ConversationColumns, String>): Int {
        return writableDatabase.delete(
            table.tableName, where.asWhereQuery(), where.values.toTypedArray()
        )
    }

    private fun update(where: HashMap<ConversationColumns, String>, values: ContentValues): Int {
        return writableDatabase.update(
            table.tableName, values, where.asWhereQuery(), where.values.toTypedArray()
        )
    }


    fun deleteByConversationId(conversationId: Long) {
        delete(where = hashMapOf(CONVERSATION_ID to conversationId.toString()))
    }

    fun getConversationsByInboxId(inboxId: Long): List<ConversationEncryptedModel> {
        return getConversations(where = hashMapOf(INBOX_ID to inboxId.toString()))
    }

    fun insert(model: ConversationEncryptedModel, inboxId: Long): Long {
        return writableDatabase.insert(table.tableName,
            null,
            ContentValues().apply {
                put(PUBLIC_KEY, model.publicKeyEncrypted)
                put(INBOX_ID, inboxId)
                put(LABEL, model.labelEncrypted)
                put(HAS_NEW_MESSAGE, model.hasNewMessage)
                put(PUBLIC_KEY_IV, model.publicKeyIv.apply {
                    Log.e("inserted public key iv", model.publicKeyIv)
                })
                put(LABEL_IV, model.labelIv)
                put(SALT, model.salt)
            }
        )
    }

}