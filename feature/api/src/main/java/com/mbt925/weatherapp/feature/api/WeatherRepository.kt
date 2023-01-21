package com.mbt925.weatherapp.feature.api

import com.mbt925.weatherapp.core.data.DataAccess
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit

interface WeatherRepository {

    fun getData(
        temperatureUnit: TemperatureUnit,
        windSpeedUnit: WindSpeedUnit,
    ): DataAccess<WeatherDataResult>

}
