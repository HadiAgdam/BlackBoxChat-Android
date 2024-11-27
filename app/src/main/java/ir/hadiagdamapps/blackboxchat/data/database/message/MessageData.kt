package ir.hadiagdamapps.blackboxchat.data.database.message

import android.content.ContentValues
import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.Error
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.models.message.EncryptedLocalMessage
import ir.hadiagdamapps.blackboxchat.data.database.message.MessageColumns.*
import ir.hadiagdamapps.blackboxchat.data.database.put

class MessageData(context: Context) : DatabaseHelper(context, Table.MESSAGES) {

    private fun insert(encryptedLocalMessage: EncryptedLocalMessage): Long {
        return writableDatabase.insert(table.tableName, null, ContentValues().apply {
            put(MESSAGE_ID, encryptedLocalMessage.messageId)
            put(CONVERSATION_ID, encryptedLocalMessage.conversationId)
            put(TEXT, encryptedLocalMessage.encryptedText)
            put(SENT, encryptedLocalMessage.sent)
            put(IV, encryptedLocalMessage.iv)

        })
    }


    fun newMessage(encryptedLocalMessage: EncryptedLocalMessage) {
        if (insert(encryptedLocalMessage) == -1L)
            throw Error.NOT_UNIQUE_MESSAGE_ID // I know it is possible to get -1 but not because its not unique
    }


}