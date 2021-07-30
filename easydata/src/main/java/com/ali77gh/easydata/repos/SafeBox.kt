package com.ali77gh.easydata.repos

import android.content.Context
import com.ali77gh.easydata.security.Encryption
import com.ali77gh.easydata.security.Encryption.generateKey
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