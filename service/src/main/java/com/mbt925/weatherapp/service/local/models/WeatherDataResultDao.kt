package com.mbt925.weatherapp.service.local.models

import com.mbt925.weatherapp.networking.adapters.LocalDateTimeSerializer
import java.time.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class WeatherDataResultDao(
    val data: WeatherDataDao? = null,
)

@Serializable
data class WeatherDataDao(
    val temperatureUnit: TemperatureUnitDao,
    val windSpeedUnit: WindSpeedUnitDao,
    val now: WeatherDao,
    val today: List<WeatherDao>,
)

@Serializable
data class WeatherDao(
    val dateTime: @Serializable(LocalDateTimeSerializer::class) LocalDateTime,
    val temperature: Float,
    val pressure: Float,
    val windSpeed: Float,
    val humidity: Int,
    val typeName: String,
)

enum class TemperatureUnitDao { C, F }

enum class WindSpeedUnitDao { Kmh, Mph }
