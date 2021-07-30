package com.ali77gh.easydata.security

import java.lang.RuntimeException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

/**
 * Created by ali on 8/12/18.
 */
object Encryption {

    //key should be 16 or 32 bytes
    fun generateKey(key: String): SecretKey {
        return when(key.length){
            0->throw RuntimeException("empty key")
            32 -> SecretKeySpec(key.toByteArray(), "AES")
            in 1..32 -> generateKey(key + key)
            else -> generateKey(key.substring(0,32))
        }
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