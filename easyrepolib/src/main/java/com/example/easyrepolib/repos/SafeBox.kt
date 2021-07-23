package com.example.easyrepolib.repos

import android.content.Context
import com.example.easyrepolib.security.Encryption
import com.example.easyrepolib.security.Encryption.generateKey
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * Created by ali on 8/30/18.
 */
class SafeBox(context: Context, key: String) {

    private var secretKey: SecretKey = generateKey(key)
    private val byteRepo: ByteDAO = ByteDAO(context, RootMode.LOCAL)

    fun save(fileName: String, sensitiveData: String) =
            byteRepo.save(
                    fileName,
                    Encryption.encrypt(sensitiveData, secretKey)
            )

    fun load(fileName: String) =
            Encryption.decrypt(
                    byteRepo.load(fileName),
                    secretKey
            )
}