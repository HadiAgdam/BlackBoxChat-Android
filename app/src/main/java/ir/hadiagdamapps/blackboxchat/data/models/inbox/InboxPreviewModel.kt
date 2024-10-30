package ir.hadiagdamapps.blackboxchat.data.models.inbox

import ir.hadiagdamapps.blackboxchat.data.models.Label
import ir.hadiagdamapps.blackboxchat.data.models.PublicKey

data class InboxPreviewModel(
    val inboxId: Long,
    val publicKey: PublicKey,
    val label: Label
)