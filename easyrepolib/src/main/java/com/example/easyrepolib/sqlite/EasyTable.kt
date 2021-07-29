package com.example.easyrepolib.sqlite

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

abstract class EasyTable<T : Model>(
        context: Context,
        private val type: Class<T>,
        tableName: String = type.simpleName,
        private var autoSetId: Boolean = false
) : KeyValDb(context,tableName) , Iterable<T>{

    // Insert
    open fun insert(row: T) {
        if (autoSetId) row.id = UUID.randomUUID().toString()
        add(row.id, row)
    }

    open fun insertMany(rows:Iterable<T>) = rows.forEach { insert(it) }

    // Update
    open fun update(row: T) = super.update(row.id, row)

    open fun updateMany(rows: Iterable<T>) = rows.forEach { update(it.id, it) }

    open fun updateAll(change:(row:T)->T) = updateMany(map(change))

    open fun updateWhere(condition: (obj: T) -> Boolean, change:(row:T)->T)
            = updateMany(filter(condition).map(change))


    //Delete
    open fun deleteWhere(condition: (obj: T) -> Boolean)
            = deleteMany(filter(condition).map { it.id }.toTypedArray())


    //Read
    open fun toList(): ArrayList<T> {
        val list = ArrayList<T>()
        val itr = iterator()
        while (itr.hasNext())
            list.add(itr.next())
        return list
    }

    /**
     * @return null if not found
     */
    open fun getById(id: String) = gson.fromJson<T>(super.getByIdStr(id),type)

    /**
     *  this is faster then filter { it.id = ids.contains() }
     */
    open fun getByIds(id: List<String>) = super.getByIdsStr(id).map { gson.fromJson(it,type) }

    /**
     * @return null if not found
     */
    open fun getOne(condition: (obj: T) -> Boolean):T?{
        for(row in this)
            if (condition(row)) return row
        return null
    }

    override fun iterator(): Iterator<T> {
        val itr = super.innerIterator()
        return object : Iterator<T> {
            override fun hasNext(): Boolean {
                return itr.hasNext()
            }

            override fun next(): T {
                return gson.fromJson(
                        itr.next(),
                        type
                ) as T
            }
        }
    }
}