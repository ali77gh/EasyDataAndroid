package com.example.easyrepolib.sqlite

import android.content.Context
import java.util.*

abstract class EasyTable<T : Model?>(
    context: Context,
    private val type: Class<*>,
    tableName: String = type.simpleName,
    private var autoSetId: Boolean=false
){
    private var db = KeyValDb(context,tableName)

    //Read
    val all: List<T> get() = db.readAllOfType(type) as List<T>

    fun getById(id: String) = db.get(id, type) as T

    fun getWithCondition(condition:(obj:T) -> Boolean) =
            db.readWithCondition(condition as ((obj:Any)->Boolean), type) as List<T>

    val isEmpty: Boolean get() = db.isEmpty

    // Write
    fun insert(newRow: T) {
        if (autoSetId) newRow!!.id = UUID.randomUUID().toString()
        db.add(newRow!!.id, newRow)
    }

    fun update(obj: T) = db.update(obj!!.id, obj)

    fun delete(id: String) = db.remove(id)

    fun drop() = db.drop()
}