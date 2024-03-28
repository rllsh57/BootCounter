package com.github.rllsh57.boot.counter.boot

import android.content.*
import androidx.core.content.ContextCompat
import com.github.rllsh57.boot.counter.ACTION_BOOT_COMPLETED


class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, BootCounterService::class.java))
    }
}

fun registerBroadcastReceiver(context: Context) {
    ContextCompat.registerReceiver(
        context,
        BootCompletedReceiver(),
        IntentFilter(ACTION_BOOT_COMPLETED),
        ContextCompat.RECEIVER_EXPORTED
    )
    ContextCompat.registerReceiver(
        context,
        BootCompletedReceiver(),
        IntentFilter(Intent.ACTION_BOOT_COMPLETED),
        ContextCompat.RECEIVER_EXPORTED
    )
}

fun sendBroadcast(context: Context) {
    val intent = Intent(ACTION_BOOT_COMPLETED)
    context.sendBroadcast(intent)
}