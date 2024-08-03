package com.urdubolo.com.pk.Util


import android.app.Application
import com.urdubolo.com.pk.Util.MySharedPref

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MySharedPref.init(this)
    }
}
