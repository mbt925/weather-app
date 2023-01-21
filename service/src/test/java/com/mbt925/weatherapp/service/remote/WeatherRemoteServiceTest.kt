package com.mbt925.weatherapp.service.remote

import com.mbt925.weatherapp.networking.adapters.LocalDateTimeSerializer
import com.mbt925.weatherapp.service.remote.model.HourlyDto
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDisplayNameDto
import com.mbt925.weatherapp.service.remote.model.UnitsDto
import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDisplayNameDto
import java.time.LocalDateTime
import java.time.Month
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherRemoteServiceTest {

    @Test
    fun getWeatherData() {

        val expected = WeatherDataDto(
            units = UnitsDto(
                temperatureUnit = TemperatureUnitDisplayNameDto.Fahrenheit,
                windSpeedUnit = WindSpeedUnitDisplayNameDto.Kmh,
            ),
            hourly = HourlyDto(
                dateTime = listOf(
                    LocalDateTime.of(2021, Month.FEBRUARY, 21, 0, 0),
                    LocalDateTime.of(2022, Month.JANUARY, 21, 1, 0),
                ),
                temperature = listOf(5.6f, 0.5f),
                humidity = listOf(90, 93),
                windSpeed = listOf(2.3f, 5.7f),
                pressure = listOf(1028.1f,1032.6f),
                weatherCode = listOf(1, 2)
            )
        )

        val payload = """
            {"generationtime_ms":0.6320476531982422,"utc_offset_seconds":0,"timezone":"GMT","timezone_abbreviation":"GMT","elevation":13.0,"hourly_units":{"time":"iso8601","temperature_2m":"°F","relativehumidity_2m":"%","windspeed_10m":"km/h"},"hourly":{"time":["2021-02-21T00:00","2022-01-21T01:00"],"temperature_2m":[5.6,0.5],"relativehumidity_2m":[90,93],"windspeed_10m":[2.3,5.7],"pressure_msl":[1028.1,1032.6],"weathercode":[1,2]}}
            """

        assertEquals(
            expected,
            json.decodeFromString(WeatherDataDto.serializer(), payload),
        )

    }

    private val json = Json {
        serializersModule = SerializersModule {
            contextual(LocalDateTimeSerializer)
        }
        ignoreUnknownKeys = true
    }
}
