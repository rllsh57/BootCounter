package com.github.rllsh57.boot.counter.boot

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

const val DEFAULT_DISMISSAL_INTERVAL = 25L * 60L * 1000L  // 25 min in milliseconds
const val DEFAULT_TOTAL_DISMISSALS = 5

const val SHARED_PREFS_NAME = "boot_counter"
const val KEY_BOOT_TIMESTAMPS = "boot_timestamps"
const val KEY_TOTAL_DISMISSALS = "boot_total_dismissals"
const val KEY_DISMISSALS_INTERVAL = "boot_dismissal_interval"

class BootTimeStorage(
    context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun addTimestamp(timestamp: Long) {
        val list = readAll().toMutableList()
        list.add(timestamp)
        val json = Json.encodeToString<List<Long>>(Json.serializersModule.serializer(), list)
        sharedPreferences.edit().putString(KEY_BOOT_TIMESTAMPS, json).commit()
    }

    fun readAll(): List<Long> {
        val json = sharedPreferences.getString(KEY_BOOT_TIMESTAMPS, "[]") ?: "[]"
        return Json.decodeFromString<List<Long>>(json)
    }

    fun readTotalDismissals(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_DISMISSALS, DEFAULT_TOTAL_DISMISSALS)
    }

    fun writeTotalDismissals(totalDismissals: Int) {
        sharedPreferences.edit().putInt(KEY_TOTAL_DISMISSALS, totalDismissals).commit()
    }

    fun readDismissalInterval(): Long {
        return sharedPreferences.getLong(KEY_DISMISSALS_INTERVAL, DEFAULT_DISMISSAL_INTERVAL)
    }

    fun writeDismissalInterval(interval: Long) {
        sharedPreferences.edit().putLong(KEY_DISMISSALS_INTERVAL, interval).commit()
    }

    fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}