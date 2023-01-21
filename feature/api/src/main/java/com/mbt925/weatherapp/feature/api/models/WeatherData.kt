package com.mbt925.weatherapp.feature.api.models

import java.time.OffsetDateTime

data class WeatherData(
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val now: Weather,
    val today: List<Weather>,
)

data class Weather(
    val dateTime: OffsetDateTime,
    val temperature: Float,
    val pressure: Float,
    val windSpeed: Float,
    val humidity: Int,
    val type: WeatherType,
)
