package com.ali77gh.easydataexample

import android.content.Context
import com.ali77gh.easydata.IORun
import com.ali77gh.easydata.sqlite.EasyTable
import com.ali77gh.easydata.sqlite.Model

/**
 * Created by ali on 8/23/18.
 */

class Loc(val lat:Double,val lng:Double)
class User(
        override var id: String,
        var hashPass: String,
        var name: String,
        var age :Int,
        var role:String,
        var money:Int,
        var marks :List<Float>?=null,
        var locations: List<Loc>?=null
) : Model {

    class UserTable(context: Context) :
           EasyTable<User>(context, User::class.java,autoSetId = false) {

        // custom queries here
        fun getByName(name: String) = filter { it.name==name }

        val admins get() = filter { it.role=="admin" }

        fun isAdmin(id: String) = any { it.id==id && it.role=="admin" } // reusable

        val top5Richs get() = sortedByDescending { it.money }.subList(0,5)

        fun checkPassword(id:String, hashPass:String) = getById(id)!!.hashPass==hashPass

        fun isUnderAge(id:String) = any{ it.id==id && it.age<18 }

        fun removeUnderAges() = deleteWhere { it.age < 18 }

        fun increaseAges1() = updateAll {
            it.age++
            return@updateAll it
        }

        fun increaseAges2() = updateAll {it.age++;it}

        fun increaseAges3() = updateAll {it.apply{ age++ }}

        fun increaseRoleOfAlis() = updateWhere({it.name=="ali"},{it.role="admin";it})

        fun asyncGetByName(name:String,cb:(user: User)->Unit)
                = IORun( {filter { it.name==name }[0]} , cb )

    }

    // repo singleton
    companion object {
        private var repo: UserTable? = null
        fun getRepo(context: Context): UserTable {
            if (repo ==null) repo = UserTable(context)
            return repo!!
        }
    }
}