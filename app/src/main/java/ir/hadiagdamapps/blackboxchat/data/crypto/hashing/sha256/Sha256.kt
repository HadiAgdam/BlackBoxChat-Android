package ir.hadiagdamapps.blackboxchat.data.crypto.hashing.sha256

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Sha256 {

    fun hash(pin: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(pin.toByteArray(StandardCharsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}