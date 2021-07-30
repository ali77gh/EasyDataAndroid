package com.ali77gh.easydataexample

import android.content.Context
import com.ali77gh.easydata.repos.StringDAO

class Settings private constructor(context: Context) {

    private val repo = StringDAO(context)

    var theme
        get() = repo.load("theme","dark")
        set(value) = repo.save("theme",value)

    var notification : Boolean
        get() = repo.load("notification","true").toBoolean()
        set(value) = repo.save("notification",value.toString())

    var lastLogin : Long
        get() = repo.load("lastLogin","0").toLong()
        set(value) = repo.save("lastLogin",value.toString())

    enum class DateSystem{Jalali,Hijri,Gregorian}
    var dateSystem : DateSystem
        get() = DateSystem.valueOf(repo.load("dateSystem", DateSystem.Jalali.name))
        set(value) = repo.save("dateSystem",value.name)

    companion object{
        var settings: Settings?= null
        fun get(context: Context): Settings {
           if (settings ==null) settings = Settings(context)
           return settings!!
        }
    }
}