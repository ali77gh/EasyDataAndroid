package com.example.ali.easyrepo

import android.content.Context
import com.example.easyrepolib.sqlite.EasyTable
import com.example.easyrepolib.sqlite.Model

/**
 * Created by ali on 8/23/18.
 */
class User(
        override var id: String,
        var hashPass: String,
        var name: String,
        var age :Int,
        var role:String,
        var money:Int
) : Model {

    class UserTable(context: Context) :
            EasyTable<User>(context, User::class.java) {

        // custom queries here
        fun getByName(name: String) = getWithCondition { it.name==name }

        val admins get() = getWithCondition { it.role=="admin" }

        fun isAdmin(id: String) = admins.any { it.id==id }

        val top5Richs get() = all.sortedByDescending { it.money }.subList(0,5)

        fun checkPassword(id:String, hashPass:String) = getById(id).hashPass==hashPass

    }

    // repo singleton
    companion object {
        private var repo: UserTable? = null
        fun getRepo(context: Context): UserTable {
            if (repo==null) repo = UserTable(context)
            return repo!!
        }
    }
}