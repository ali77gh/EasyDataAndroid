package com.example.easyrepolib.security

import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

/**
 * Created by ali on 8/12/18.
 */
object Encryption {

    //key should be 16 or 32 bytes
    fun generateKey(key: String): SecretKey {
        if (key.length != 16 && key.length != 32) throw RuntimeException("key length should be 16 or 32 characters")
        return SecretKeySpec(key.toByteArray(), "AES")
    }

    fun encrypt(message: String, secret: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        return cipher.doFinal(message.toByteArray())
    }

    fun decrypt(cipherText: ByteArray?, secret: SecretKey): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher.doFinal(cipherText))
    }
}