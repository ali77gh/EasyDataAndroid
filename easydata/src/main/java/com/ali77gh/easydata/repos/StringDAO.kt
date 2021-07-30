package com.ali77gh.easydata.repos

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.*

/**
 * Created by ali on 8/22/18.
 */
class StringDAO(
        context: Context,
        rootMode: RootMode = RootMode.LOCAL
) : GRepo(context,rootMode) {

    /**
     * @param default default value of default is ""
     * @return default if not exist
     */
    fun load(fileName: String,default:String=""): String {
        if (!checkExist(fileName)) return default

        val path = "$root/$fileName"

        val inputStream = FileInputStream(File(path))
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var receiveString: String?
        val stringBuilder = StringBuilder()
        while (bufferedReader.readLine().also { receiveString = it } != null) {
            stringBuilder.append(receiveString)
        }
        inputStream.close()
        return stringBuilder.toString()
    }

    fun loadAsync(filename: String, callback: (data:String)->Unit) {
        Thread {
            val data = load(filename)
            Handler(Looper.getMainLooper()).post { callback(data) }
        }.start()
    }

    fun save(fileName: String, data: String) {
        val path = "$root/$fileName"
        Log.d("yohogooo_path",path)
        val outputStreamWriter = OutputStreamWriter(FileOutputStream(File(path)))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    }

    fun saveAsync(filename: String, data: String, callback: ()->Unit={}) {
        Thread {
            save(filename,data)
            Handler(Looper.getMainLooper()).post { callback() }
        }.start()
    }
}