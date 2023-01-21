package com.mbt925.weatherapp.feature.data.mapper

import com.mbt925.weatherapp.core.utils.toInstant
import com.mbt925.weatherapp.core.utils.toOffsetDateTime
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.Weather
import com.mbt925.weatherapp.feature.api.models.WeatherData
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WeatherType
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.data.ClockProvider
import com.mbt925.weatherapp.feature.data.weatherDataDto
import com.mbt925.weatherapp.networking.models.ApiResponse
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDisplayNameDto
import com.mbt925.weatherapp.service.remote.model.UnitsDto
import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDisplayNameDto
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.Test

class RemoteDataAdapterTest {

    private val clockProvider = mockk<ClockProvider>()

    @Test
    fun success() {
        val nowDate = LocalDateTime.of(2000, 1, 1, 0, 0)
        every { clockProvider.now() } returns nowDate.toInstant()
        val nextHour = nowDate.plusHours(1)

        val fullData = WeatherData(
            temperatureUnit = TemperatureUnit.Fahrenheit,
            windSpeedUnit = WindSpeedUnit.MilesPerHour,
            now = Weather(
                dateTime = nowDate.toOffsetDateTime(),
                temperature = 1f,
                humidity = 3,
                windSpeed = 5f,
                pressure = 7f,
                type = WeatherType.MainlyClear,
            ),
            today = listOf(Weather(
                dateTime = nextHour.toOffsetDateTime(),
                temperature = 2f,
                pressure = 8f,
                windSpeed = 6f,
                humidity = 4,
                type = WeatherType.Overcast
            )),
        )
        val fullDataDto = WeatherDataDto(
            units = UnitsDto(
                temperatureUnit = TemperatureUnitDisplayNameDto.Fahrenheit,
                windSpeedUnit = WindSpeedUnitDisplayNameDto.Mph
            ),
            hourly = weatherDataDto.hourly.copy(
                dateTime = listOf(nowDate, nextHour),
                temperature = listOf(1f, 2f),
                humidity = listOf(3, 4),
                windSpeed = listOf(5f, 6f),
                pressure = listOf(7f, 8f),
                weatherCode = listOf(1, 3),
            )
        )
        val onlyNowDataDto = WeatherDataDto(
            units = UnitsDto(
                temperatureUnit = TemperatureUnitDisplayNameDto.Fahrenheit,
                windSpeedUnit = WindSpeedUnitDisplayNameDto.Mph
            ),
            hourly = weatherDataDto.hourly.copy(
                dateTime = listOf(nowDate),
                temperature = listOf(1f),
                humidity = listOf(3),
                windSpeed = listOf(5f),
                pressure = listOf(7f),
                weatherCode = listOf(1),
            )
        )

        val data = listOf(fullData, fullData.copy(today = emptyList()))
        val dtos = listOf(fullDataDto, onlyNowDataDto)

        val adapter = getProvider()
        data.forEachIndexed { index, d ->
            val dto = dtos[index]
            assertEquals(
                WeatherDataResult.Success(d),
                adapter.map(ApiResponse.success(dto)),
            )
        }
    }

    @Test
    fun failure() {
        val nowDate = LocalDateTime.of(2000, 1, 1, 0, 0)
        every { clockProvider.now() } returns nowDate.toInstant()

        val domain = listOf(
            WeatherDataResult.Failure.UnknownError("error"),
            WeatherDataResult.Failure.MissingNetwork,
            WeatherDataResult.Failure.UnknownError("message"),
        )
        val ioException = IOException()
        val unknownException = Throwable("message")
        val dtos = listOf(
            ApiResponse.failure(1, "{\"error\":true, \"reason\":\"error\"}"),
            ApiResponse.failure(ioException),
            ApiResponse.failure(unknownException),
        )

        val adapter = getProvider()
        domain.forEachIndexed { index, data ->
            val dto = dtos[index]
            assertEquals(
                data,
                adapter.map(dto),
            )
        }
    }

    private fun getProvider() = RemoteDataAdapterImpl(
        clockProvider = clockProvider,
    )

}
