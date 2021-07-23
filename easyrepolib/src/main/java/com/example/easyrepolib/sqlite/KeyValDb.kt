package com.example.easyrepolib.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.gson.Gson
import java.util.*

/**
 * Created by ali on 9/17/18.
 */
class KeyValDb(context: Context, private var table: String="default") {

    //TODO remove me
    private val db = context.openOrCreateDatabase("easydb", Context.MODE_PRIVATE, null)
    private val gson: Gson = Gson()

    init { db.execSQL("CREATE TABLE IF NOT EXISTS $table(id VARCHAR,value VARCHAR);") }

    fun add(id: String, obj: Any?) {
        db.execSQL("INSERT INTO $table VALUES('$id','${gson.toJson(obj)}');")
    }

    fun get(id: String, type: Class<*>?): Any {
        val resultSet = db.rawQuery("Select * from $table where id='$id';", null)
        resultSet.moveToFirst()
        val strObj = resultSet.getString(1)
        resultSet.close()
        return gson.fromJson(strObj, type)
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

    fun readAllOfType(type: Class<*>?): List<Any> {
        val validObjs: MutableList<Any> = ArrayList()
        val resultSet = db.rawQuery("Select * from $table;", null)
        resultSet.moveToFirst()
        var strObj: String?
        var obj: Any
        while (!resultSet.isAfterLast) {
            strObj = resultSet.getString(1)
            try {
                obj = gson.fromJson(strObj, type)
                validObjs.add(obj)
            } catch (e: Exception) { /*ignore*/ }
            resultSet.moveToNext()
        }
        resultSet.close()
        return validObjs
    }

    fun readWithCondition(condition:(obj:Any) -> Boolean , type: Class<*>?): List<Any> {
        val validObjs: MutableList<Any> = ArrayList()
        val resultSet = db.rawQuery("Select * from $table;", null)
        resultSet.moveToFirst()
        while (!resultSet.isAfterLast) {
            val strObj = resultSet.getString(1)
            val obj: Any = try {
                gson.fromJson(strObj, type)
            } catch (e: Exception) {
                resultSet.moveToNext()
                continue
            }
            if (condition(obj)) validObjs.add(obj)
            resultSet.moveToNext()
        }
        resultSet.close()
        return validObjs
    }

    fun update(id: String, obj: Any?) {
        val contentValues = ContentValues().apply {
            put("value", gson.toJson(obj))
        }
        db.update(table, contentValues, "id = ? ", arrayOf(id))
    }

    fun remove(vararg ids: String?) = db.delete(table, "id = ? ", ids)
    fun drop() = db.execSQL("DROP TABLE IF EXISTS $table")
}