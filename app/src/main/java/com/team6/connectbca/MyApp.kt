package com.team6.connectbca

import android.app.Application
import com.team6.connectbca.di.koinModule
import com.team6.connectbca.diviewmodel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(koinModule, viewModelModule)
        }
    }
}