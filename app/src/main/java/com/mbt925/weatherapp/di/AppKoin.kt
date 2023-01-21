package com.mbt925.weatherapp.di

import com.mbt925.weatherapp.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MainViewModel(
            model = get(),
            userPreferences = get(),
        )
    }

}
