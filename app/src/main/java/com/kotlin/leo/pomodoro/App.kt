package com.kotlin.leo.pomodoro

import android.app.Application
import android.content.Context

/**
 * Created by leo on 10/7/17.
 */
class App : Application() {

    companion object {
        lateinit var instance : App
            get
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val Context.app : App
        get() = applicationContext as App
}