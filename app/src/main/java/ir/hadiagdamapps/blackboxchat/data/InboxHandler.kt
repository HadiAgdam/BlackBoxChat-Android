package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.database.inbox.InboxData
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxPreviewModel


class InboxHandler(context: Context) {

    val data = InboxData(context)


    fun createInbox(pin: Pin): InboxModel {
        return data.newInbox(pin) ?: throw Error.INSERT_FAILED
    }


    fun getInboxes(): List<InboxPreviewModel> =
        data.getInboxes().map {
            it.toPreviewModel()
        }



    fun deleteInbox(inboxId: Long) {
        TODO("delete inbox")
    }


    fun updateLabel(text: String, inboxId: Long) {
        //TODO("update")
    }

}