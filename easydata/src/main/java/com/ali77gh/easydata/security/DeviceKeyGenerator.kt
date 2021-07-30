package com.ali77gh.easydata.security

import android.content.Context
import android.provider.Settings.Secure

/**
 * Created by ali on 8/30/18.
 */
object DeviceKeyGenerator {
    fun Generate(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }
    //recommended
    fun Generate(context: Context, secret: String): String {
        val id = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        return id.substring(secret.length) + secret
    }
}