package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import android.util.Log
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxColumns
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxData
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxPreviewModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.toPreviewModel


class InboxHandler(context: Context) {

    val data = InboxData(context)


    fun createInbox(pin: Pin): InboxModel {
        return data.newInbox(pin) ?: throw Error.INSERT_FAILED
    }


    fun getInboxes(): List<InboxPreviewModel> =
        data.getInboxes().map {
            Log.e("label", it.label.display())
            it.toPreviewModel()
        }


    fun deleteInbox(inboxId: Long) {
        data.deleteInbox(inboxId)
    }


    fun updateLabel(label: Label, inboxId: Long) {
        data.updateLabel(label, inboxId)
    }


    fun getInboxById(inboxId: Long): InboxModel {
        return data.getInboxes(hashMapOf(InboxColumns.INBOX_ID to inboxId.toString())).first() // I know this is wrong
    }

}