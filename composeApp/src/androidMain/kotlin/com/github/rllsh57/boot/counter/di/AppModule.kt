package com.github.rllsh57.boot.counter.di

import android.content.Context
import com.github.rllsh57.boot.counter.boot.BootTimeStorage
import com.github.rllsh57.boot.counter.di.AppModule.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.*
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { BootTimeStorage(get()) }
    }
}

fun initKoin(context: Context) {
    stopKoin()
    startKoin {
        androidContext(context)
        modules(appModule)
    }
}