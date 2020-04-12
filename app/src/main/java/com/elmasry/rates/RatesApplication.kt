package com.elmasry.rates

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RatesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                // use AndroidLogger as Koin Logger - default Level.INFO
                androidLogger()
            }

            // use the Android context given there
            androidContext(this@RatesApplication)

            // module list
            modules(Dependencies.get())
        }
    }
}