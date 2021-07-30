package com.ali77gh.easydata.repos

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.io.*

/**
 * Created by ali on 8/20/18.
 */
class ByteDAO(
        context: Context,
        mode: RootMode
) : GRepo(context, mode) {

    fun load(filename: String): ByteArray? {
        val path = "$root/$filename"
        val size = File(path).length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(path))
            buf.read(bytes, 0, bytes.size)
            buf.close()
            return bytes
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun loadAsync(filename: String, callback: (bytes:ByteArray?)->Unit) {
        Thread {
            val bytes = load(filename)
            Handler(Looper.getMainLooper()).post { callback(bytes) }
        }.start()
    }

    fun save(filename: String, bytes: ByteArray) {
        val path = "$root/$filename"
        val out = FileOutputStream(path)
        out.write(bytes)
        out.flush()
        out.close()
    }

    fun saveAsync(filename: String, bytes: ByteArray, callback: ()->Unit={}) {
        Thread {
            save(filename, bytes)
            Handler(Looper.getMainLooper()).post { callback() }
        }.start()
    }
}