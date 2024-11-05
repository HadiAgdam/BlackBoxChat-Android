package ir.hadiagdamapps.blackboxchat.data

import android.content.Context
import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.Pin
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxModel
import ir.hadiagdamapps.blackboxchat.data.models.inbox.InboxPreviewModel


class InboxHandler(context: Context) {


    fun createInbox(pin: Pin): InboxModel {
        return InboxModel( // temp
            0,
            PublicKey.parse("public key")!!,
            Label.create("label")!!
        )
        TODO("Create a new inbox and insert it to database using data class." +
                "If anything failed, instead of returning false, throw exception")
    }


    fun getInboxes(): List<InboxPreviewModel> {
        return List<InboxPreviewModel>(3) { // temp
            InboxModel(
                it.toLong(),
                PublicKey.parse("public_key")!!,
                Label.create("inbox $it")!!
            )
        }

        TODO("return the list of inboxes")
    }


    fun deleteInbox(inboxId: Long) {
        TODO("delete inbox")
    }


    fun updateLabel(text: String, inboxId: Long) {
        //TODO("update")
    }

}