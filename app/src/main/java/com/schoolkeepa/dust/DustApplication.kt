package com.schoolkeepa.dust

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DustApplication : Application() {

    var user = ""

    companion object{
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        preferences = PreferenceUtil(applicationContext)
        super.onCreate()
    }



}