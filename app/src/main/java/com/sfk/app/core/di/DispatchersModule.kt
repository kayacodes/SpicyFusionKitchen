package com.sfk.app.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dispatchersModule = module {
    single<CoroutineDispatcher>(DispatchersQualifiers.IO) { Dispatchers.IO }
    single<CoroutineDispatcher>(DispatchersQualifiers.DEFAULT) { Dispatchers.Default }
    single<CoroutineDispatcher>(DispatchersQualifiers.MAIN) { Dispatchers.Main }
}
