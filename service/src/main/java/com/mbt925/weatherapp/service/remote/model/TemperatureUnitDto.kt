package com.mbt925.weatherapp.service.remote.model

import kotlinx.serialization.Serializable

@Serializable
enum class TemperatureUnitDto(val key: String) {
    C("celsius"), F("fahrenheit");
}
