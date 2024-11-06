package ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e

import android.util.Log
import java.security.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import java.util.*

object E2EEncryptor {

    // Encrypt the AES key using the recipient's public key (ECIES)
    fun encryptAESKeyWithPublicKey(aesKey: SecretKey, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("ECIES")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedAESKey = cipher.doFinal(aesKey.encoded)
        return Base64.getEncoder().encodeToString(encryptedAESKey)
    }

    // Decrypt the AES key using the user's private key (ECIES)
    fun decryptAESKeyWithPrivateKey(encryptedAESKey: String, privateKey: PrivateKey): SecretKey {
        Log.e("encrypted AES decrypt", encryptedAESKey)
        val cipher = Cipher.getInstance("ECIES")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decodedAESKeyBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey))
        return SecretKeySpec(decodedAESKeyBytes, 0, decodedAESKeyBytes.size, "AES")
    }

    fun SecretKey.toText(): String {
        return Base64.getEncoder().encodeToString(encoded)
    }
}
