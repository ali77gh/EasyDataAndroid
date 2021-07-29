package com.example.easyrepolib.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.util.Log
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by ali on 9/17/18.
 */
open class KeyValDb(
    context: Context,
    private var table: String="default"
){

    private val db = context.openOrCreateDatabase("easydb", Context.MODE_PRIVATE, null)
    val gson: Gson = Gson()

    init {
        db.execSQL("CREATE TABLE IF NOT EXISTS $table(id VARCHAR PRIMARY KEY,value VARCHAR);")
    }

    fun add(id: String, obj: Any){
        db.execSQL("INSERT INTO $table VALUES('$id','${gson.toJson(obj)}');")
    }

    val isEmpty: Boolean get() {
        val resultSet = db.rawQuery("Select * from $table;", null)
        resultSet.moveToFirst()
        return try {
            resultSet.getString(1)
            false
        } catch (e: CursorIndexOutOfBoundsException) {
            true
        }
    }

    protected fun update(id: String, obj: Any) {
        val contentValues = ContentValues().apply {
            put("value", gson.toJson(obj))
        }
        db.update(table, contentValues, "id = ? ", arrayOf(id))
    }

    protected fun getByIdStr(id:String):String?{
        val cursor = db.rawQuery("Select value from $table where id='$id';", null)!!
        cursor.moveToFirst()
        if (cursor.isAfterLast) return null
        return cursor.getString(0)
    }

    /*
     * this is faster way because have no gson deserialize insides
     */
    protected fun getByIdsStr(ids:List<String>):List<String>{
        val idsMap = ids.associateBy { it }
        val result = ArrayList<String>()

        val cursor = db.rawQuery("Select * from $table;", null)!!
        cursor.moveToFirst()
        while (!cursor.isAfterLast)
            if (idsMap.containsKey(cursor.getString(0)))
                result.add(cursor.getString(1))

        return result
    }

    fun delete(id: String) = db.delete(table, "id = ? ", arrayOf(id))

    protected fun deleteMany(ids: Array<String>) = ids.forEach { delete(it) }

    fun drop() = db.execSQL("DROP TABLE IF EXISTS $table")


    // Iterable APIs
    private var resultSet = db.rawQuery("Select value from $table;", null)!!

    protected fun reset() {
        resultSet = db.rawQuery("Select value from $table;", null)!!
        resultSet.moveToFirst()
    }

    protected fun hasNext(): Boolean = !resultSet.isAfterLast

    protected fun next(): String {
        val str = resultSet.getString(0)
        resultSet.moveToNext()
        return str
    }
}