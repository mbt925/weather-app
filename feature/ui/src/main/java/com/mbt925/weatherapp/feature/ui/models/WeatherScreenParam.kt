package com.mbt925.weatherapp.feature.ui.models

import androidx.annotation.StringRes
import com.mbt925.weatherapp.feature.ui.R
import java.time.OffsetDateTime

data class WeatherScreenParam(
    val isLoading: Boolean = true,
    val offline: Boolean = true,
    val temperatureUnit: TemperatureUnitParam = TemperatureUnitParam.Celsius,
    val windSpeedUnit: WindSpeedUnitParam = WindSpeedUnitParam.KilometresPerHour,
    val nowWeather: WeatherParam? = null,
    val todayWeather: List<WeatherParam>? = null,
    val failureParam: FailureParam? = null,
    val todayRows: Int = 1,
    val hasVerticalLayout: Boolean = true,
)

data class WeatherParam(
    val dateTime: OffsetDateTime,
    val temperature: Float,
    val pressure: Int,
    val windSpeed: Int,
    val humidity: Int,
    val type: WeatherTypeParam,
)

sealed interface FailureParam {
    val message: Int

    data class GpsOff(
        @StringRes override val message: Int = R.string.error_gps_disabled
    ) : FailureParam

    data class PermissionDenied(
        @StringRes override val message: Int = R.string.error_permission_denied
    ) : FailureParam

    data class NoInternet(
        @StringRes override val message: Int = R.string.error_no_internet
    ) : FailureParam

    data class UnknownError(
        @StringRes override val message: Int = R.string.error_unknown,
        val reason: String,
    ) : FailureParam
}
