package com.sfk.app.feature.home.di

import com.sfk.app.feature.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel() }
}
