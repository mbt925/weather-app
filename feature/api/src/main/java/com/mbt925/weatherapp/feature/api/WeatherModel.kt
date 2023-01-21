package com.mbt925.weatherapp.feature.api

import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import kotlinx.coroutines.flow.StateFlow

interface WeatherModel {
    suspend fun fetchWeatherData(
        temperatureUnit: TemperatureUnit,
        windSpeedUnit: WindSpeedUnit,
    )

    val state: StateFlow<WeatherState>
}
