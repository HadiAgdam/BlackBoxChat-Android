package ir.hadiagdamapps.blackboxchat.data.database.pending_message

import android.content.ContentValues
import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.database.generateWhereQuery
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.message.OutgoingMessage
import ir.hadiagdamapps.blackboxchat.data.database.pending_message.PendingMessageColumns.*
import ir.hadiagdamapps.blackboxchat.data.database.put

class PendingMessageData(context: Context) : DatabaseHelper(context, Table.MESSAGES) {

    private fun getAll(where: HashMap<PendingMessageColumns, String>? = null): List<OutgoingMessage> {
        val c = readableDatabase.rawQuery(
            """
            SELECT
                $MESSAGE_ID,
                $RECEIVER,
                $ENCRYPTION_KEY,
                $IV,
                $ENCRYPTED_MESSAGE
            from ${table.tableName}
            ${generateWhereQuery(where)}
        """.trimIndent(), where?.values?.toTypedArray()
        )

        return ArrayList<OutgoingMessage>().apply {

            if (c.moveToFirst()) do
                add(
                    OutgoingMessage(
                        pendingMessageId = c.getLong(0),
                        receiver = c.getString(1),
                        encryptionKey = c.getString(2),
                        iv = c.getString(3),
                        encryptedMessage = c.getString(4)
                    )
                )
            while (c.moveToNext())

            c.close()
        }
    }

    fun getByReceiver(publicKey: PublicKey): List<OutgoingMessage> =
        getAll(hashMapOf(RECEIVER to publicKey.display()))


    fun insert(
        outgoingMessage: OutgoingMessage
    ) {
        writableDatabase.insert(table.tableName, null, ContentValues().apply {
            put(MESSAGE_ID, outgoingMessage.pendingMessageId)
            put(RECEIVER, outgoingMessage.receiver)
            put(ENCRYPTED_MESSAGE, outgoingMessage.encryptedMessage)
            put(IV, outgoingMessage.iv)
            put(ENCRYPTED_MESSAGE, outgoingMessage.encryptedMessage)
        })
    }

    fun deleteById(pendingMessageId: Long) {
        writableDatabase.delete(table.tableName, "$MESSAGE_ID = ?", arrayOf(pendingMessageId.toString()))
    }
}