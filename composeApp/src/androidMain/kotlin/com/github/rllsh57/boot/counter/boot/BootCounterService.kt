package com.github.rllsh57.boot.counter.boot

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.koin.android.ext.android.get

class BootCounterService : Service() {

    private val storage: BootTimeStorage = get()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification(this, storage.readAll())
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}