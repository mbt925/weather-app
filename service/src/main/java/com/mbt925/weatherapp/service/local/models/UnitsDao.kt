package com.mbt925.weatherapp.service.local.models

import kotlinx.serialization.Serializable


@Serializable
data class UnitsDao(
    val temperature: TemperatureUnitDao = TemperatureUnitDao.C,
    val windSpeed: WindSpeedUnitDao = WindSpeedUnitDao.Kmh,
)
