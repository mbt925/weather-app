package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TemperatureUnitDisplayNameDto {
    @SerialName("°C")
    Celsius,

    @SerialName("°F")
    Fahrenheit;
}
