package com.mbt925.weatherapp

import android.app.Application
import com.mbt925.weatherapp.di.appModule
import com.mbt925.weatherapp.feature.di.featureModule
import com.mbt925.weatherapp.networking.di.networkingModule
import com.mbt925.weatherapp.service.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(
                listOf(
                    appModule,
                    featureModule,
                    networkingModule,
                    serviceModule,
                )
            )
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        stopKoin()
    }

}
