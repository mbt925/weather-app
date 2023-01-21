package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.networking.executeApi
import com.mbt925.weatherapp.networking.models.ApiResponse
import com.mbt925.weatherapp.service.remote.LatitudeWrapper
import com.mbt925.weatherapp.service.remote.LongitudeWrapper
import com.mbt925.weatherapp.service.remote.TemperatureUnitWrapper
import com.mbt925.weatherapp.service.remote.WeatherService
import com.mbt925.weatherapp.service.remote.WindSpeedUnitWrapper
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDto
import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke

interface WeatherRemoteDataSource {

    suspend fun fetchWeatherData(
        latitude: LatitudeWrapper,
        longitude: LongitudeWrapper,
        temperatureUnit: TemperatureUnitDto,
        windSpeedUnit: WindSpeedUnitDto,
    ): ApiResponse<WeatherDataDto>
}

internal class WeatherRemoteDataSourceImpl(
    private val service: WeatherService,
    private val dispatcher: CoroutineDispatcher,
) : WeatherRemoteDataSource {

    override suspend fun fetchWeatherData(
        latitude: LatitudeWrapper,
        longitude: LongitudeWrapper,
        temperatureUnit: TemperatureUnitDto,
        windSpeedUnit: WindSpeedUnitDto,
    ): ApiResponse<WeatherDataDto> = dispatcher {
        service.getWeatherData(
            latitude = latitude,
            longitude = longitude,
            tempUnit = TemperatureUnitWrapper(temperatureUnit.key),
            windSpeedUnit = WindSpeedUnitWrapper(windSpeedUnit.key),
        ).executeApi()
    }
}
