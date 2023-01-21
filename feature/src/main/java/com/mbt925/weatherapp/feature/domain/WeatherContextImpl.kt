package com.mbt925.weatherapp.feature.domain

import com.mbt925.weatherapp.core.domain.Context
import com.mbt925.weatherapp.feature.api.WeatherModel
import com.mbt925.weatherapp.feature.api.WeatherRepository
import com.mbt925.weatherapp.feature.api.WeatherState
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit

internal class WeatherContextImpl(
    private val weatherRepository: WeatherRepository,
) : Context<WeatherState> by Context(
    initialState = WeatherState(),
), WeatherModel {

    override suspend fun fetchWeatherData(
        temperatureUnit: TemperatureUnit,
        windSpeedUnit: WindSpeedUnit,
    ) {
        val fetchDataUseCase = FetchWeatherDataUseCase(
            weatherRepository = weatherRepository,
            temperatureUnit = temperatureUnit,
            windSpeedUnit = windSpeedUnit,
        )

        execute(fetchDataUseCase)
    }
}
