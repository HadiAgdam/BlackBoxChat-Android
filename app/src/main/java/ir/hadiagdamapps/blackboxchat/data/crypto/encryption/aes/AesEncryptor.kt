package ir.hadiagdamapps.blackboxchat.data.crypto.encryption.aes

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object AesEncryptor {

    fun encryptMessage(message: String, aesKey: SecretKey): Pair<String, String> {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)

        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec)
        val encryptedMessage = cipher.doFinal(message.toByteArray(Charsets.UTF_8))

        return Pair(Base64.getEncoder().encodeToString(encryptedMessage), Base64.getEncoder().encodeToString(iv))
    }


    fun decryptMessage(encryptedMessage: String, aesKey: SecretKey, ivString: String): String? {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = Base64.getDecoder().decode(ivString)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec)
        val decryptedMessage: ByteArray
        try {
            decryptedMessage = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))
        }
        catch (ex: Exception) {
            return null
        }

        return String(decryptedMessage, Charsets.UTF_8)
    }
}