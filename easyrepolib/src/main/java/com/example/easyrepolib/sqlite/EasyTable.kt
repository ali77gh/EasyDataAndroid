package com.example.easyrepolib.sqlite

import android.content.Context
import java.util.*

abstract class EasyTable<T : Model?>(
        context: Context,
        private val type: Class<*>,
        tableName: String = type.simpleName,
        private var autoSetId: Boolean=false
) : KeyValDb(context,tableName) , Iterable<T>{

    // Insert
    fun insert(row: T) {
        if (autoSetId) row!!.id = UUID.randomUUID().toString()
        add(row!!.id, row)
    }

    fun insertMany(rows:Iterable<T>) = rows.forEach { insert(it) }

    // Update
    fun update(row: T) = super.update(row!!.id, row)

    fun updateMany(rows: Iterable<T>) = rows.forEach { update(it!!.id, it) }

    inline fun updateAll(change:(row:T)->T) = updateMany(map(change))

    inline fun updateWhere(condition: (obj: T) -> Boolean, change:(row:T)->T)
            = updateMany(filter(condition).map(change))


    //Delete
    inline fun deleteWhere(condition: (obj: T) -> Boolean)
            = deleteMany(filter(condition).map { it!!.id }.toTypedArray())


    //Read
    val all: List<T> get() = readAllOfType(type) as List<T>

    fun getById(id: String) = get(id, type) as T

    override fun iterator(): Iterator<T> {

        reset()
        return object : Iterator<T> {
            override fun hasNext(): Boolean {
                return this@EasyTable.hasNext()
            }

            override fun next(): T {
                return gson.fromJson(
                        this@EasyTable.next(),
                        this@EasyTable.type
                ) as T
            }
        }
    }
}