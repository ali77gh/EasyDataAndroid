package com.example.easyrepolib.repos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.io.File
import java.io.FileOutputStream

/**
 * Created by ali on 8/20/18.
 */
class BitmapDAO(
        context: Context,
        mode: RootMode
) : GRepo(context, mode) {

    @SuppressWarnings("loading bitmap in main thread not recommended (use loadSync)")
    fun load(fileName: String): Bitmap? {
        val path = "$root/$fileName"
        val f = File(path)
        return if (f.exists())
            BitmapFactory.decodeFile(path)
        else null
    }

    fun loadAsync(filename: String, callback: (bitmap:Bitmap?)->Unit) {
        Thread {
            val bitmap = load(filename)
            Handler(Looper.getMainLooper()).post { callback(bitmap) }
        }.start()
    }

    @SuppressWarnings("loading bitmap in main thread not recommended (use saveAsync)")
    fun save(
            filename: String,
            bitmap: Bitmap,
            quality:Int=100,
            width:Int=bitmap.width,
            height:Int=bitmap.height
    ) {
        val path = "$root/$filename"
        val out = FileOutputStream(path)
        val resized = Bitmap.createScaledBitmap(bitmap,width , height, false)
        resized.compress(Bitmap.CompressFormat.PNG, quality, out)
        out.flush()
        out.close()
    }

    fun saveAsync(
            filename: String,
            bitmap: Bitmap,
            quality:Int=100,
            width:Int=bitmap.width,
            height:Int=bitmap.height,
            callback: ()->Unit
    ) {
        Thread {
            save(filename, bitmap,quality,width,height)
            Handler(Looper.getMainLooper()).post { callback() }
        }.start()
    }
}