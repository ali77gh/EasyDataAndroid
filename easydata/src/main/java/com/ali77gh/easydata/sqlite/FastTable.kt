package com.ali77gh.easydata.sqlite

import android.content.Context
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class FastTable<T : Model>(
        context: Context,
        private val type: Class<T>,
        tableName: String = type.simpleName,
        autoSetId: Boolean = false
) : EasyTable<T>(context ,type,tableName,autoSetId){

//    private var IOWriteTasks = ArrayList<()->Unit>()

    private val cache : HashMap<String,T>

    init {
        if (CachePool.containsKey(tableName))
            cache = CachePool[tableName] as HashMap<String, T>
        else{
            cache = HashMap()
            for (row in super.toList())
                cache[row.id] = row
            CachePool[tableName] = cache as HashMap<String, Model>
        }

//        Thread{
//            while (true){
//                if (IOWriteTasks.size != 0){
//                    IOWriteTasks[0].invoke()
//                    IOWriteTasks.removeAt(0)
//                } else Thread.sleep(100) // CPU need to sleep sometimes :) (for battery life)
//            }
//        }.start()
    }

    override fun insert(row: T) {
        cache[row.id] = row
        super.insert(row)
    }

    override fun insertMany(rows:Iterable<T>){
        for (row in rows)
            cache[row.id] = row
        super.insertMany(rows)
    }

    // Update
    override fun update(row: T){
        cache[row.id] = row
        super.update(row)
    }

    override fun updateMany(rows: Iterable<T>){
        for (row in rows)
            cache[row.id] = row
        super.updateMany(rows)
    }

    override fun updateAll(change:(row:T)->T){
        for (row in this)
            cache[row.id] = change(row)
        super.updateAll(change)
    }

    override fun updateWhere(condition: (obj: T) -> Boolean, change:(row:T)->T){
        super.updateWhere(condition, change)
        for (row in this)
            if (condition(row))
                cache[row.id] = change(row)
    }


    //Delete
    override fun deleteWhere(condition: (obj: T) -> Boolean){
        super.deleteWhere(condition) // this will not work if you put it after loop
        for (row in this.toList())
            if (condition(row))
                cache.remove(row.id)
    }

    //Read
    override fun toList() = ArrayList<T>().apply {
        for (row in this@FastTable)
            this@apply.add(row)
    }

    /**
     * @return null if not found
     * */
    override fun getById(id: String):T?{
        return cache[id]
    }

    /**
     * @return null if not found
     * */
    override fun getOne(condition: (obj: T) -> Boolean):T?{
        val iterator = this.iterator()
        while (iterator.hasNext()){
            val row = iterator.next()
            if (condition(row)) return row
        }
        return null
    }

    override fun iterator(): Iterator<T> {
        val iterator = cache.iterator()
        return object : Iterator<T> {

            override fun hasNext() = iterator.hasNext()

            override fun next() : T = iterator.next().value
        }
    }

    companion object{
        private val CachePool = HashMap<String,HashMap<String,Model>>()

        fun clearCache(tableName:String){
            CachePool.remove(tableName)
        }
        fun clearCache(type:Class<Model>){
            clearCache(type.simpleName)
        }
    }
}