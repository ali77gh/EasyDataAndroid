package com.ali77gh.easydataexample

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.ali77gh.easydataexample.R
import com.ali77gh.easydata.repos.*
import com.ali77gh.easydata.security.DeviceKeyGenerator
import com.google.gson.Gson
import java.util.*
import kotlin.system.measureTimeMillis

class TestActivity : Activity() {
    private var log: TextView? = null
    private var image: ImageView? = null

    // Tools
    private val bitmapForTest: Bitmap
        get() = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
    private fun log(msg: String){
        Log.d("test",msg)
        log!!.append("$msg\n")
    }
    private fun logTitle(title:String) = log("\n----- $title -----")
    private fun test(name:String,bool:Boolean) = log(name+"\t\t\t" + if (bool) " passed!" else " failed! ")
    private fun test(name:String,a:Any,b:Any) = test(name,a==b)
    fun logUI(msg:String)=runOnUiThread { log(msg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        log = findViewById(R.id.logger)
        image = findViewById(R.id.image)

//        bitmapTest()
//        byteTest()
//        stringTest()
//        objectTest()
//        genericDAOTest()
//        genericDAOTestHeavy()
//        testSafeBox()
//        performanceTest()
        testSettings()
    }

    private fun bitmapTest() {
        logTitle("bitmap test")

        val bitmapDAO = BitmapDAO(this, RootMode.LOCAL)

        // Sync
        bitmapDAO.save("testBitmap.bmp",bitmapForTest,100)
        test("testBitmap.bmp",bitmapDAO.checkExist("testBitmap.bmp"))

        bitmapDAO.save("testBitmap50.bmp",bitmapForTest,50)
        test("testBitmap50.bmp",bitmapDAO.checkExist("testBitmap50.bmp"))

        bitmapDAO.save("testBitmap10px.bmp",bitmapForTest, width=10, height=10)
        test("testBitmap10px.bmp",bitmapDAO.checkExist("testBitmap10px.bmp"))

        // Async
        bitmapDAO.saveAsync("testBitmapAsync.bmp", bitmapForTest, callback = {
            test("testBitmapAsync.bmp",bitmapDAO.checkExist("testBitmapAsync.bmp"))
        })
        Thread.sleep(1000)
        log("bitmap tests all done!")
    }

    private fun byteTest() {

        logTitle("byte test")

        val byteDAO = ByteDAO(this, RootMode.LOCAL)
        val bytes = ByteArray(4)
        bytes[0] = 1
        bytes[1] = 1
        bytes[2] = 0
        bytes[3] = 1

        // saving
        byteDAO.save("test", bytes)
        test("saving",byteDAO.checkExist("test"))

        // loading
        val loadedBytes: ByteArray = byteDAO.load("test")!!
        test("byte eq:",loadedBytes[0],bytes[0])

        // removing
        byteDAO.remove("test")
        test("removing",!byteDAO.checkExist("test"))
    }

    private fun stringTest() {

        logTitle("string test")

        val stringDAO = StringDAO(this, RootMode.LOCAL)
        val string = "write this file"

        //saving
        stringDAO.save("test", string)
        test("saving",stringDAO.checkExist("test"))

        // loading
        val loadedString: String = stringDAO.load("test")
        test("byte eq:",loadedString,string)

        // removing
        stringDAO.remove("test")
        test("removing",!stringDAO.checkExist("test"))

        // default
        test("default",stringDAO.load("test","def"),"def")
    }

    private fun objectTest() {

        logTitle("object test")

        val dao = ObjectDAO(this, RootMode.LOCAL)
        val user = User("id","pass","ali",18,"admin",1000)

        dao.save("test", user)
        test("saving",dao.checkExist("test"))

        // loading
        val loaded: User = dao.load("test", User::class.java) as User
        test("equality:",loaded.name,user.name)

        // removing
        dao.remove("test")
        test("removing",!dao.checkExist("test"))
    }

    private fun genericDAOTest() {

        logTitle("generic test")
        val users = User.getRepo(this)
        val g = Gson()

        log("---before delete---")
        for(user in users)
            log(user.name)

        users.deleteWhere { true }

        log("---after delete---")
        for(user in users)
            log(user.name)

        //insert
        val u = User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000)
        users.insert(u)

        log("---after insert---")
        for(user in users)
            log(user.id)

        test("insert",users.getById(u.id)!!.name,"ali")

        //read
        test("read",users.toList().size,1)

        //update
        u.name = "hasan"
        users.update(u)
        test("update",users.getById(u.id)!!.name,"hasan")

        //delete
        users.deleteWhere { it.id==u.id }
        test("delete",users.toList().size,0)

    }

    private fun genericDAOTestHeavy() {

        logTitle("generic test heavy")

        val users = User.getRepo(this)
        users.deleteWhere { true }

        log("---after delete all")
        for(user in users)
            log(user.id)

        users.filter { true }

        //insert
        users.insert(User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000))
        users.insert(User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000))
        users.insert(User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000))
        users.insert(User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000))

        log("---after 4 inserts---")
        for(user in users)
            log(user.id)

        log("---filter---")
        for(user in users.filter { it.id.contains("e") })
            log(user.id)

        log(users.sumBy { it.age }.toString())
    }

    private fun performanceTest(){

        logTitle("performance")

        Thread{
            val users = User.getRepo(this)

            logUI("users size: ${users.toList().size}")

            val L = 10000
            val g = Gson()
            val serializeTime = measureTimeMillis {
                for(i in 0..L){
                    val user = User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000)
                    g.toJson(user)
                }
            }
            logUI("serializeTime: $serializeTime")
            val user = User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000)
            val str = g.toJson(user)
            val deserializeTime = measureTimeMillis {
                for(i in 0..L){
                    g.fromJson<User>(str, User::class.java)
                }
            }
            logUI("deserializeTime: $deserializeTime")

            val r = Random()
            val insertTime = measureTimeMillis {
                 for(i in 0..L)
                     users.insert(User(UUID.randomUUID().toString(),"pass","ali",18,"admin",1000))
            }
            logUI("users size: ${users.toList().size}")
            logUI(" insert:                    $insertTime ms")

            val lastId = users.toList().last().id
            val getById = measureTimeMillis {
                users.getById(lastId)
            }
            logUI(" getById:               $getById ms")

            val getWithFalsyFilter = measureTimeMillis {
                users.filter { false }
            }
            logUI(" getWithFalsyFilter:               $getWithFalsyFilter ms")

            val readAllTime = measureTimeMillis {
                users.toList()
            }
            logUI(" readAllTime:               $readAllTime ms")

            val readWithFilterTime = measureTimeMillis {
                users.filter { it.name=="ali" }
            }
            logUI(" readWithFilterTime:        $readWithFilterTime ms")
            val readWithFilterAndMapTime = measureTimeMillis {
                users.filter { it.name=="ali" }.map { it.role }
            }
            logUI(" readWithFilterAndMapTime:  $readWithFilterAndMapTime ms")
            val readWithMapAndFilterTime = measureTimeMillis {
                users.map { it.name }.filter { it=="ali" }
            }
            logUI(" readWithMapAndFilterTime:  $readWithMapAndFilterTime ms")
            val updateAll = measureTimeMillis {
                users.updateWhere({it.name=="ali"},{it.apply { it.age=23 }})
            }
            logUI(" updateAll:                 $updateAll ms")

            val updateOne = measureTimeMillis {
                users.updateWhere({it.id==lastId},{it.apply { it.age=25 }})
            }
            logUI(" updateOne:                 $updateOne ms")

            val removeOne = measureTimeMillis {
                users.deleteWhere { it.id==lastId }
            }
            logUI(" removeOne:                 $removeOne ms")

            val removeAll = measureTimeMillis {
                users.deleteWhere { true }
            }
            logUI(" removeAll:                 $removeAll ms")
        }.start()
    }

    private fun testSafeBox() {
        logTitle("safebox test")
        val key = DeviceKeyGenerator.Generate(this)
        val safeBox = SafeBox(this, key)

        safeBox.save("password", "myPassword")
        var readed = safeBox.load("password")
        test("read eq",readed=="myPassword")

        safeBox.save("password", "سلام")
        readed = safeBox.load("password")
        test("utf8",readed=="سلام")
    }

    private fun testSettings(){
        logTitle("settings test")
        val now = Date().time

        Settings.get(this).theme = "dark"
        Settings.get(this).dateSystem = Settings.DateSystem.Gregorian
        Settings.get(this).notification = true
        Settings.get(this).lastLogin = now

        test("theme", Settings.get(this).theme, "dark")
        test("dateSystem", Settings.get(this).dateSystem, Settings.DateSystem.Gregorian)
        test("notif", Settings.get(this).notification, true)
        test("lastLogin", Settings.get(this).lastLogin, now)
    }
}