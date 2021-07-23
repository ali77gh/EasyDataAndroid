package com.example.easyrepolib.repos

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson

/**
 * Created by ali on 8/22/18.
 */
class ObjectDAO(
        context: Context,
        mode: RootMode
) : GRepo(context,mode) {

    private val gson = Gson()
    private val stringRepo = StringDAO(context,mode)

    fun load(filename: String, type: Class<*>) =
            gson.fromJson(
                    stringRepo.load(filename),
                    type
            )

    fun loadAsync(filename: String, type: Class<*>, callback: (data:Any)->Unit) {
        Thread {
            val obj = load(filename, type)
            Handler(Looper.getMainLooper()).post { callback(obj) }
        }.start()
    }

    fun save(filename: String, obj: Any) =
        stringRepo.save(filename, gson.toJson(obj))

    fun saveAsync(filename: String, obj: Any, callback: ()->Unit={}) {
        Thread {
            save(filename, obj)
            Handler(Looper.getMainLooper()).post { callback() }
        }.start()
    }
}