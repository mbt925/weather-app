package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName

enum class TemperatureUnitDisplayNameDto {
    @SerialName("°C")
    Celsius,

    @SerialName("°F")
    Fahrenheit;
}
