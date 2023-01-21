package com.mbt925.weatherapp.service.remote.model

import com.mbt925.weatherapp.networking.adapters.LocalDateTimeSerializer
import java.time.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyDto(
    @SerialName("time")
    val dateTime: List<@Serializable(LocalDateTimeSerializer::class) LocalDateTime>,
    @SerialName("temperature_2m")
    val temperature: List<Float>,
    @SerialName("relativehumidity_2m")
    val humidity: List<Int>,
    @SerialName("windspeed_10m")
    val windSpeed: List<Float>,
    @SerialName("pressure_msl")
    val pressure: List<Float>,
    @SerialName("weathercode")
    val weatherCode: List<Int>,
)
