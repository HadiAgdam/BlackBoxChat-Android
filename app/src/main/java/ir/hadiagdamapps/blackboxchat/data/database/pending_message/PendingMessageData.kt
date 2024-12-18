package ir.hadiagdamapps.blackboxchat.data.database.pending_message

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.database.generateWhereQuery
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.message.OutgoingMessage
import ir.hadiagdamapps.blackboxchat.data.database.pending_message.PendingMessageColumns.*

class PendingMessageData(context: Context) : DatabaseHelper(context, Table.MESSAGES) {

    private fun getAll(where: HashMap<PendingMessageColumns, String>? = null): List<OutgoingMessage> {
        TODO()
    }

    fun getByReceiver(publicKey: PublicKey): List<OutgoingMessage> =
        getAll(hashMapOf(RECEIVER to publicKey.display()))


    fun insert(
        outgoingMessage: OutgoingMessage
    ) {
        TODO()
    }

    fun deleteById(pendingMessageId: Long) {
        writableDatabase.delete(table.tableName, "$MESSAGE_ID = ?", arrayOf(pendingMessageId.toString()))
    }
}