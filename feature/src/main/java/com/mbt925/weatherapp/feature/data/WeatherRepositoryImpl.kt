package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.core.data.DataAccess
import com.mbt925.weatherapp.core.data.invoke
import com.mbt925.weatherapp.feature.api.WeatherRepository
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult
import com.mbt925.weatherapp.feature.api.models.LocationResult
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult.Failure
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.data.mapper.LocalDataAdapter
import com.mbt925.weatherapp.feature.data.mapper.RemoteDataAdapter
import com.mbt925.weatherapp.feature.data.mapper.toDto
import com.mbt925.weatherapp.service.local.WeatherLocalDataSource
import com.mbt925.weatherapp.service.remote.LatitudeWrapper
import com.mbt925.weatherapp.service.remote.LongitudeWrapper

internal class WeatherRepositoryImpl(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val locationDataSource: LocationDataSource,
    private val remoteDataAdapter: RemoteDataAdapter,
    private val localDataAdapter: LocalDataAdapter,
) : WeatherRepository {

    override fun getData(
        temperatureUnit: TemperatureUnit,
        windSpeedUnit: WindSpeedUnit,
    ) = DataAccess(
        get = {
            localDataSource.getWeatherData()?.let {
                localDataAdapter.map(it)
            }
        },
        save = { data ->
            val success = data?.let(localDataAdapter::map)?.let {
                localDataSource.setWeatherData(it)
                true
            } ?: false
            success
        },
        fetch = {
            when (val locationResult = locationDataSource.getCurrentLocation()) {
                is LocationResult.Success -> {
                    val result = remoteDataSource.fetchWeatherData(
                        latitude = LatitudeWrapper(locationResult.location.latitude),
                        longitude = LongitudeWrapper(locationResult.location.longitude),
                        temperatureUnit = temperatureUnit.toDto(),
                        windSpeedUnit = windSpeedUnit.toDto(),
                    )
                    remoteDataAdapter.map(result)
                }
                is LocationAccessResult.Failure -> Failure.MissingLocation(
                    error = locationResult
                )
            }
        }
    )

}
