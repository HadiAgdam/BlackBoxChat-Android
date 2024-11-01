package ir.hadiagdamapps.blackboxchat.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class Clipboard(private val context: Context) {

    private val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // copy text to clipboard
    fun copy(text: String, label: String = "BlackBox") {
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun readClipboard(): String? {
        return if (clipboard.hasPrimaryClip() && (clipboard.primaryClip
                ?: return null).itemCount > 0
        ) {
            clipboard.primaryClip!!.getItemAt(0)!!.text.toString()
        } else null
    }
}