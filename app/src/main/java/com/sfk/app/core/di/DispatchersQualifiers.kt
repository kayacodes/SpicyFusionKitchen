package com.sfk.app.core.di

import org.koin.core.qualifier.named

object DispatchersQualifiers {
    val IO = named("io")
    val DEFAULT = named("default")
    val MAIN = named("main")
}
