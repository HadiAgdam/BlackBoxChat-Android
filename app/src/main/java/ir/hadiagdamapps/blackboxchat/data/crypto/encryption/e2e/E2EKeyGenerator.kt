package ir.hadiagdamapps.blackboxchat.data.crypto.encryption.e2e

import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object E2EKeyGenerator {

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(256)  // Recommended key size for ECC
        return keyPairGenerator.generateKeyPair()
    }


    fun PublicKey.toText(): String {
        return Base64.getEncoder().encodeToString(this.encoded)
    }

    fun getPublicKeyFromString(text: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(text)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(keySpec)
    }

    fun PrivateKey.toText(): String {
        return Base64.getEncoder().encodeToString(this.encoded)
    }

    fun getPrivateKeyFromString(text: String): PrivateKey {
        val keyBytes = Base64.getDecoder().decode(text)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePrivate(keySpec)
    }

}