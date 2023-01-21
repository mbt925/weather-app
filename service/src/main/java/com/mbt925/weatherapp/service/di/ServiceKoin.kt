package com.mbt925.weatherapp.service.di

import com.mbt925.weatherapp.service.local.UserPreferences
import com.mbt925.weatherapp.service.local.UserPreferencesImpl
import com.mbt925.weatherapp.service.local.WeatherLocalDataSource
import com.mbt925.weatherapp.service.local.WeatherLocalDataSourceImpl
import com.mbt925.weatherapp.service.local.userPreferences
import com.mbt925.weatherapp.service.local.weatherDataResultDataSource
import com.mbt925.weatherapp.service.remote.WeatherService
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val serviceModule = module {

    single { get<Retrofit>().create(WeatherService::class.java) }

    factory<WeatherLocalDataSource> {
        WeatherLocalDataSourceImpl(
            dataStore = androidContext().weatherDataResultDataSource,
            dispatcher = Dispatchers.IO,
        )
    }
    factory<UserPreferences> {
        UserPreferencesImpl(
            dataStore = androidContext().userPreferences,
            dispatcher = Dispatchers.IO,
        )
    }


}
