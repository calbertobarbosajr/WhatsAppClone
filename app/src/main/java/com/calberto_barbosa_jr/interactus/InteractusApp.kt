package com.calberto_barbosa_jr.interactus

import android.app.Application
import com.calberto_barbosa_jr.interactus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InteractusApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InteractusApp)
            modules(appModule)
        }
    }
}