package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.core.utils.toOffsetDateTime
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.Weather
import com.mbt925.weatherapp.feature.api.models.WeatherData
import com.mbt925.weatherapp.feature.api.models.WeatherType
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.service.local.models.TemperatureUnitDao
import com.mbt925.weatherapp.service.local.models.WeatherDao
import com.mbt925.weatherapp.service.local.models.WeatherDataDao
import com.mbt925.weatherapp.service.local.models.WindSpeedUnitDao
import com.mbt925.weatherapp.service.remote.model.HourlyDto
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDisplayNameDto
import com.mbt925.weatherapp.service.remote.model.UnitsDto
import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDisplayNameDto
import java.time.LocalDateTime


internal val weatherDataDao = WeatherDataDao(
    temperatureUnit = TemperatureUnitDao.F,
    windSpeedUnit = WindSpeedUnitDao.Kmh,
    now = WeatherDao(
        dateTime = LocalDateTime.now(),
        temperature = 1f,
        humidity = 2,
        windSpeed = 3f,
        pressure = 4f,
        typeName = WeatherType.MainlyClear.name,
    ),
    today = emptyList(),
)

internal val weatherDataDto = WeatherDataDto(
    units = UnitsDto(
        temperatureUnit = TemperatureUnitDisplayNameDto.Fahrenheit,
        windSpeedUnit = WindSpeedUnitDisplayNameDto.Kmh,
    ),
    hourly = HourlyDto(
        dateTime = listOf(weatherDataDao.now.dateTime),
        temperature = listOf(1f),
        humidity = listOf(2),
        windSpeed = listOf(3f),
        pressure = listOf(4f),
        weatherCode = listOf(1),
    )
)

internal val weatherData = WeatherData(
    temperatureUnit = TemperatureUnit.Fahrenheit,
    windSpeedUnit = WindSpeedUnit.KilometresPerHour,
    now = Weather(
        weatherDataDao.now.dateTime.toOffsetDateTime(),
        temperature = 1f,
        humidity = 2,
        windSpeed = 3f,
        pressure = 4f,
        type = WeatherType.MainlyClear,
    ),
    today = emptyList(),
)
