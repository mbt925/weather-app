package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.Serializable

@Serializable
enum class WindSpeedUnitDto(val key: String) {
    Mph("mph"), Kmh("kmh");
}
