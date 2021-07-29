package com.example.easyrepolib

import android.os.Handler
import android.os.Looper


fun <T> IORun(run:()->T,cb:(v:T)->Unit){
    Thread{
        val result = run()
        Handler(Looper.getMainLooper()).post { cb(result) }
    }.start()
}