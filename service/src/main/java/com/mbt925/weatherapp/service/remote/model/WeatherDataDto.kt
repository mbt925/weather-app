package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataDto(
    @SerialName("hourly_units")
    val units: UnitsDto,
    @SerialName("hourly")
    val hourly: HourlyDto,
)
