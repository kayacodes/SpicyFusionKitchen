package com.sfk.app

import android.app.Application
import com.sfk.app.core.di.appModule
import com.sfk.app.core.di.dispatchersModule
import com.sfk.app.feature.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SfkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SfkApp)
            modules(
                appModule,
                dispatchersModule,
                homeModule
            )
        }
    }
}
