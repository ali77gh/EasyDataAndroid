package com.example.ali.easyrepo

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.easyrepolib.repos.*
import com.example.easyrepolib.security.DeviceKeyGenerator.Generate

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        log = findViewById(R.id.logger)
        image = findViewById(R.id.image)

//        bitmapTest()
        byteTest()
        stringTest()
        objectTest()
        genericDAOTest()
        testSafeBox()
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
        val loaded: User = dao.load("test",User::class.java) as User
        test("equality:",loaded.name,user.name)

        // removing
        dao.remove("test")
        test("removing",!dao.checkExist("test"))
    }

    private fun genericDAOTest() {

        logTitle("generic test")

        val users = User.getRepo(this)

        val u = User("id","pass","ali",18,"admin",1000)

        //insert
        users.insert(u)

        test("insert",users.getById("id").name,"ali")

        //read
        test("read",users.all.size,1)

        //update
        u.name = "hasan"
        users.update(u)
        test("update",users.getById(u.id).name,"hasan")

        //delete
        users.delete(u.id)
        test("delete",users.all.size,0)

        users.drop()
    }

    private fun testSafeBox() {
        logTitle("safebox test")
        val key = Generate(this)
        val safeBox = SafeBox(this, key)

        safeBox.save("password", "myPassword")
        var readed = safeBox.load("password")
        test("read eq",readed=="myPassword")

        safeBox.save("password", "سلام")
        readed = safeBox.load("password")
        test("utf8",readed=="سلام")
    }
}