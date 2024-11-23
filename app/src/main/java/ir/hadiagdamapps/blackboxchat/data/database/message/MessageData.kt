package ir.hadiagdamapps.blackboxchat.data.database.message

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.database.DatabaseHelper
import ir.hadiagdamapps.blackboxchat.data.database.Table
import ir.hadiagdamapps.blackboxchat.data.models.message.EncryptedLocalMessage

class MessageData(context: Context) : DatabaseHelper(context, Table.MESSAGES){

    fun insert(encryptedLocalMessage: EncryptedLocalMessage) {

    }


}