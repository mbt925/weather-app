package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnitsDto(
    @SerialName("temperature_2m")
    val temperatureUnit: TemperatureUnitDisplayNameDto,
    @SerialName("windspeed_10m")
    val windSpeedUnit: WindSpeedUnitDisplayNameDto,
)
