package com.example.secveneyatest

import android.app.Application
import com.example.secveneyatest.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}