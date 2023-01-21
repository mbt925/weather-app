package com.mbt925.weatherapp.feature.ui.models

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.time.OffsetDateTime

internal class WeatherScreenParamProvider : PreviewParameterProvider<WeatherScreenParam> {
    private val weather1 = WeatherParam(
        dateTime = OffsetDateTime.MIN,
        temperature = 20.5f,
        windSpeed = 5,
        humidity = 52,
        pressure = 15,
        type = WeatherTypeParam.ClearSky,
    )
    private val weather2 = WeatherParam(
        dateTime = OffsetDateTime.MAX,
        temperature = 20.5f,
        windSpeed = 5,
        humidity = 52,
        pressure = 15,
        type = WeatherTypeParam.Foggy,
    )

    override val values = sequenceOf(
        WeatherScreenParam(
            isLoading = true,
        ),
        WeatherScreenParam(
            isLoading = false,
            nowWeather = weather1,
        ),
        WeatherScreenParam(
            isLoading = false,
            temperatureUnit = TemperatureUnitParam.Fahrenheit,
            windSpeedUnit = WindSpeedUnitParam.MilesPerHour,
            nowWeather = weather2,
            todayWeather = listOf(weather1, weather2),
            todayRows = 2,
        ),
        WeatherScreenParam(
            isLoading = false,
            temperatureUnit = TemperatureUnitParam.Fahrenheit,
            windSpeedUnit = WindSpeedUnitParam.MilesPerHour,
            nowWeather = weather2,
            todayWeather = listOf(weather1, weather2),
            hasVerticalLayout = false,
            todayRows = 2,
        ),
    )

}
