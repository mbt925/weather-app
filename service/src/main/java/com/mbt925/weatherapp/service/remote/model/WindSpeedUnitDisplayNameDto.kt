package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WindSpeedUnitDisplayNameDto {
    @SerialName("km/h")
    Kmh,

    @SerialName("mp/h")
    Mph;
}
