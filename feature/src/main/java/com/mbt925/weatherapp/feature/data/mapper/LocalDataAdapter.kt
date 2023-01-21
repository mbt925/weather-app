package com.mbt925.weatherapp.feature.data.mapper

import com.mbt925.weatherapp.core.utils.toOffsetDateTime
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.Weather
import com.mbt925.weatherapp.feature.api.models.WeatherData
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WeatherType
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.service.local.models.TemperatureUnitDao
import com.mbt925.weatherapp.service.local.models.WeatherDao
import com.mbt925.weatherapp.service.local.models.WeatherDataDao
import com.mbt925.weatherapp.service.local.models.WeatherDataResultDao
import com.mbt925.weatherapp.service.local.models.WindSpeedUnitDao

internal interface LocalDataAdapter {
    fun map(result: WeatherDataResult): WeatherDataResultDao?
    fun map(result: WeatherDataResultDao): WeatherDataResult?
}

internal class LocalDataAdapterImpl : LocalDataAdapter {

    override fun map(result: WeatherDataResult): WeatherDataResultDao? {
        return when (result) {
            is WeatherDataResult.Success -> WeatherDataResultDao(result.data.toWeatherDataDao())
            else -> null
        }
    }

    override fun map(result: WeatherDataResultDao): WeatherDataResult? {
        return result.data?.let {
            WeatherDataResult.Success(it.toWeatherData())
        }
    }

    private fun WeatherDataDao.toWeatherData() = WeatherData(
        temperatureUnit = temperatureUnit.toTemperatureUnit(),
        windSpeedUnit = windSpeedUnit.toWindSpeedUnit(),
        now = now.toWeather(),
        today = today.map { it.toWeather() },
    )

    private fun WeatherData.toWeatherDataDao() = WeatherDataDao(
        temperatureUnit = temperatureUnit.toTemperatureUnitDao(),
        windSpeedUnit = windSpeedUnit.toWindSpeedUnitDao(),
        now = now.toWeatherDao(),
        today = today.map { it.toWeatherDao() },
    )

    private fun TemperatureUnitDao.toTemperatureUnit() = when (this) {
        TemperatureUnitDao.C -> TemperatureUnit.Celsius
        TemperatureUnitDao.F -> TemperatureUnit.Fahrenheit
    }

    private fun WindSpeedUnitDao.toWindSpeedUnit() = when (this) {
        WindSpeedUnitDao.Kmh -> WindSpeedUnit.KilometresPerHour
        WindSpeedUnitDao.Mph -> WindSpeedUnit.MilesPerHour
    }

    private fun TemperatureUnit.toTemperatureUnitDao() = when (this) {
        TemperatureUnit.Celsius -> TemperatureUnitDao.C
        TemperatureUnit.Fahrenheit -> TemperatureUnitDao.F
    }

    private fun WindSpeedUnit.toWindSpeedUnitDao() = when (this) {
        WindSpeedUnit.KilometresPerHour -> WindSpeedUnitDao.Kmh
        WindSpeedUnit.MilesPerHour -> WindSpeedUnitDao.Mph
    }

    private fun WeatherDao.toWeather(): Weather = Weather(
        dateTime = dateTime.toOffsetDateTime(),
        temperature = temperature,
        pressure = pressure,
        windSpeed = windSpeed,
        humidity = humidity,
        type = WeatherType.valueOf(typeName)
    )

    private fun Weather.toWeatherDao(): WeatherDao = WeatherDao(
        dateTime = dateTime.toLocalDateTime(),
        temperature = temperature,
        pressure = pressure,
        windSpeed = windSpeed,
        humidity = humidity,
        typeName = type.name,
    )
}

fun TemperatureUnitDao.toDomain() = when (this) {
    TemperatureUnitDao.C -> TemperatureUnit.Celsius
    TemperatureUnitDao.F -> TemperatureUnit.Fahrenheit
}

fun WindSpeedUnitDao.toDomain() = when (this) {
    WindSpeedUnitDao.Kmh -> WindSpeedUnit.KilometresPerHour
    WindSpeedUnitDao.Mph -> WindSpeedUnit.MilesPerHour
}
