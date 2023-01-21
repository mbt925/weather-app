package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName

enum class WindSpeedUnitDisplayNameDto {
    @SerialName("km/h")
    Kmh,

    @SerialName("mp/h")
    Mph;
}
