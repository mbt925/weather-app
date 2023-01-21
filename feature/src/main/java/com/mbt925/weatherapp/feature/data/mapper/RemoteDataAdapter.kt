package com.mbt925.weatherapp.feature.data.mapper

import com.mbt925.weatherapp.core.utils.toOffsetDateTime
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.Weather
import com.mbt925.weatherapp.feature.api.models.WeatherData
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.data.ClockProvider
import com.mbt925.weatherapp.networking.models.ApiResponse
import com.mbt925.weatherapp.service.remote.model.HourlyDto
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDisplayNameDto
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDto
import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDisplayNameDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDto
import java.io.IOException

internal interface RemoteDataAdapter {

    fun map(response: ApiResponse<WeatherDataDto>): WeatherDataResult
}

internal class RemoteDataAdapterImpl(
    private val clockProvider: ClockProvider,
) : RemoteDataAdapter {

    override fun map(response: ApiResponse<WeatherDataDto>): WeatherDataResult =
        when (response) {
            is ApiResponse.Success -> WeatherDataResult.Success(
                data = requireNotNull(response.data).toWeatherData()
            )
            is ApiResponse.Failure.Known -> WeatherDataResult.Failure.UnknownError(response.message)
            is ApiResponse.Failure.Unknown -> when (val error = response.error) {
                is IOException -> WeatherDataResult.Failure.MissingNetwork
                else -> WeatherDataResult.Failure.UnknownError(
                    message = error.message ?: error::class.toString()
                )
            }
        }

    private fun WeatherDataDto.toWeatherData(): WeatherData {
        val clusteredWeathers = hourly.cluster()

        return WeatherData(
            temperatureUnit = units.temperatureUnit.toTemperatureUnit(),
            windSpeedUnit = units.windSpeedUnit.toWindSpeedUnit(),
            now = clusteredWeathers.first,
            today = clusteredWeathers.second,
        )
    }

    private fun TemperatureUnitDisplayNameDto.toTemperatureUnit() = when (this) {
        TemperatureUnitDisplayNameDto.Celsius -> TemperatureUnit.Celsius
        TemperatureUnitDisplayNameDto.Fahrenheit -> TemperatureUnit.Fahrenheit
    }

    private fun WindSpeedUnitDisplayNameDto.toWindSpeedUnit() = when (this) {
        WindSpeedUnitDisplayNameDto.Kmh -> WindSpeedUnit.KilometresPerHour
        WindSpeedUnitDisplayNameDto.Mph -> WindSpeedUnit.MilesPerHour
    }

    private fun HourlyDto.cluster(): Pair<Weather, List<Weather>> {
        val nows = mutableListOf<Weather>()
        val todays = mutableListOf<Weather>()

        val todayDate = clockProvider.now().toOffsetDateTime()
        val todayLocalDate = todayDate.toLocalDate()

        dateTime.forEachIndexed { index, localDateTime ->
            val offsetDateTime = localDateTime.toOffsetDateTime()
            val localDate = offsetDateTime.toLocalDate()

            val weather = Weather(
                dateTime = offsetDateTime,
                temperature = temperature[index],
                pressure = pressure[index],
                windSpeed = windSpeed[index],
                humidity = humidity[index],
                type = weatherCode[index].toWeatherType()
            )

            if (localDate == todayLocalDate) {
                if (offsetDateTime.hour == todayDate.hour) nows.add(weather)
                else if (offsetDateTime.hour > todayDate.hour) todays.add(weather)
            }
        }

        return Pair(nows.first(), todays)
    }

}

internal fun TemperatureUnit.toDto() = when (this) {
    TemperatureUnit.Celsius -> TemperatureUnitDto.C
    TemperatureUnit.Fahrenheit -> TemperatureUnitDto.F
}

internal fun WindSpeedUnit.toDto() = when (this) {
    WindSpeedUnit.KilometresPerHour -> WindSpeedUnitDto.Kmh
    WindSpeedUnit.MilesPerHour -> WindSpeedUnitDto.Mph
}
