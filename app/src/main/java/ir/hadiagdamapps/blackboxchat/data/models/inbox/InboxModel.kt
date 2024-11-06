package ir.hadiagdamapps.blackboxchat.data.models.inbox

import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.PrivateKey
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey

data class InboxModel(
    val inboxId: Long,
    val inboxPublicKey: PublicKey,
    val inboxPrivateKey: PrivateKey,
    val label: Label,
    val hasNewMessage: Boolean,
    val iv: String,
    val salt: String,
)