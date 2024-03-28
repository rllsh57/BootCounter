package com.github.rllsh57.boot.counter.boot

import android.content.*
import androidx.core.app.*
import com.github.rllsh57.boot.counter.*
import kotlinx.datetime.*
import java.text.SimpleDateFormat
import java.time.*
import java.util.Locale
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

const val CHANNEL_ID = "boot_counter"

data class NotificationData(
    val title: String? = null,
    val text: String? = null
)

fun createChannel(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    val channel = NotificationChannelCompat.Builder(
        CHANNEL_ID,
        NotificationManagerCompat.IMPORTANCE_DEFAULT
    )
        .setName("Boot Counter")
        .build()
    notificationManager.createNotificationChannel(channel)
}

@Throws(SecurityException::class)
fun showNotification(context: Context, bootTimestamps: List<Long>) {

    val dateFormatter = SimpleDateFormat("DD/MM/YYYY HH:MM:SS", Locale.ENGLISH)

    val data = when (bootTimestamps.size) {
        0 -> {
            NotificationData(text = "No boots detected")
        }
        1 -> {
            val last = dateFormatter.format(bootTimestamps.last())
            NotificationData(text = "The boot was detected = $last")
        }
        else -> {
            val last = bootTimestamps.takeLast(2)
            val delta = abs(last[0] - last[1]).milliseconds
            NotificationData(text = "Last boots time delta = $delta")
        }
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(data.title)
        .setContentText(data.text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_CALL)
        .build()

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(0, notification)
}
