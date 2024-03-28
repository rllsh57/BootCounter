package com.github.rllsh57.boot.counter

import android.app.Application
import com.github.rllsh57.boot.counter.boot.*
import com.github.rllsh57.boot.counter.di.initKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
        createChannel(this)
        registerBroadcastReceiver(this)
    }
}