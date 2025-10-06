package com.sfk.app.core.di

import android.app.Application
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import org.koin.dsl.module

val appModule = module {
    single<ImageLoader> {
        ImageLoader.Builder(get<Application>())
            .crossfade(true)
            .components { add(SvgDecoder.Factory()) }
            .logger(DebugLogger())
            .build()
    }
}
