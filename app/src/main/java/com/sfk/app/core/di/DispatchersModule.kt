package com.sfk.app.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DispatchersQualifiers {
    val IO = named("io")
    val DEFAULT = named("default")
    val MAIN = named("main")
}

val dispatchersModule = module {
    single<CoroutineDispatcher>(DispatchersQualifiers.IO) { Dispatchers.IO }
    single<CoroutineDispatcher>(DispatchersQualifiers.DEFAULT) { Dispatchers.Default }
    single<CoroutineDispatcher>(DispatchersQualifiers.MAIN) { Dispatchers.Main }
}
