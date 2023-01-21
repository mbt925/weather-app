package com.mbt925.weatherapp.feature.api

import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit

data class WeatherState(
    val temperatureUnit: TemperatureUnit? = null,
    val windSpeedUnit: WindSpeedUnit? = null,
    val loading: Boolean = false,
    val offline: Boolean = true,
    val weather: WeatherDataResult.Success? = null,
    val failure: WeatherDataResult.Failure? = null,
)
