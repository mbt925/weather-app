package com.mbt925.weatherapp.feature.ui.mapper

import com.mbt925.weatherapp.feature.api.WeatherState
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult.Failure.GpsDisabled
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult.Failure.NoCourseLocationAccessPermission
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult.Failure.NoFineLocationAccessPermission
import com.mbt925.weatherapp.feature.api.models.LocationResult.Failure.Cancelled
import com.mbt925.weatherapp.feature.api.models.LocationResult.Failure.NoInternet
import com.mbt925.weatherapp.feature.api.models.LocationResult.Failure.Unknown
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.Weather
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult.Failure
import com.mbt925.weatherapp.feature.api.models.WeatherType
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.ui.models.FailureParam
import com.mbt925.weatherapp.feature.ui.models.TemperatureUnitParam
import com.mbt925.weatherapp.feature.ui.models.WeatherParam
import com.mbt925.weatherapp.feature.ui.models.WeatherScreenParam
import com.mbt925.weatherapp.feature.ui.models.WeatherTypeParam
import com.mbt925.weatherapp.feature.ui.models.WindSpeedUnitParam

fun WeatherState.toWeatherDataParam() = WeatherScreenParam(
    isLoading = loading,
    offline = offline,
    temperatureUnit = temperatureUnit.toParam(),
    windSpeedUnit = windSpeedUnit.toParam(),
    nowWeather = weather?.data?.now?.toParam(),
    todayWeather = weather?.data?.today?.map { it.toParam() },
    failureParam = when (val failure = failure) {
        is Failure.MissingLocation -> when (failure.error) {
            Cancelled,
            NoFineLocationAccessPermission,
            NoCourseLocationAccessPermission,
            -> FailureParam.PermissionDenied()
            GpsDisabled -> FailureParam.GpsOff()
            NoInternet -> FailureParam.NoInternet()
            Unknown -> FailureParam.UnknownError(reason = "Unknown")
        }
        Failure.MissingNetwork -> FailureParam.NoInternet()
        is Failure.UnknownError -> FailureParam.UnknownError(reason = failure.message)
        null -> null
    }
)

private fun TemperatureUnit?.toParam() = when (this) {
    TemperatureUnit.Celsius -> TemperatureUnitParam.Celsius
    TemperatureUnit.Fahrenheit -> TemperatureUnitParam.Fahrenheit
    null -> TemperatureUnitParam.Unknown
}

private fun WindSpeedUnit?.toParam() = when (this) {
    WindSpeedUnit.KilometresPerHour -> WindSpeedUnitParam.KilometresPerHour
    WindSpeedUnit.MilesPerHour -> WindSpeedUnitParam.MilesPerHour
    null -> WindSpeedUnitParam.Unknown
}

private fun Weather.toParam() = WeatherParam(
    dateTime = dateTime,
    temperature = temperature,
    pressure = pressure.toInt(),
    windSpeed = windSpeed.toInt(),
    humidity = humidity,
    type = type.toParam(),
)

private fun WeatherType.toParam(): WeatherTypeParam = when (this) {
    WeatherType.ClearSky -> WeatherTypeParam.ClearSky
    WeatherType.MainlyClear -> WeatherTypeParam.MainlyClear
    WeatherType.PartlyCloudy -> WeatherTypeParam.PartlyCloudy
    WeatherType.Overcast -> WeatherTypeParam.Overcast
    WeatherType.Foggy -> WeatherTypeParam.Foggy
    WeatherType.DepositingRimeFog -> WeatherTypeParam.DepositingRimeFog
    WeatherType.LightDrizzle -> WeatherTypeParam.LightDrizzle
    WeatherType.ModerateDrizzle -> WeatherTypeParam.ModerateDrizzle
    WeatherType.DenseDrizzle -> WeatherTypeParam.DenseDrizzle
    WeatherType.LightFreezingDrizzle -> WeatherTypeParam.LightFreezingDrizzle
    WeatherType.DenseFreezingDrizzle -> WeatherTypeParam.DenseFreezingDrizzle
    WeatherType.SlightRain -> WeatherTypeParam.SlightRain
    WeatherType.ModerateRain -> WeatherTypeParam.ModerateRain
    WeatherType.HeavyRain -> WeatherTypeParam.HeavyRain
    WeatherType.HeavyFreezingRain -> WeatherTypeParam.HeavyFreezingRain
    WeatherType.SlightSnowFall -> WeatherTypeParam.SlightSnowFall
    WeatherType.ModerateSnowFall -> WeatherTypeParam.ModerateSnowFall
    WeatherType.HeavySnowFall -> WeatherTypeParam.HeavySnowFall
    WeatherType.SnowGrains -> WeatherTypeParam.SnowGrains
    WeatherType.SlightRainShowers -> WeatherTypeParam.SlightRainShowers
    WeatherType.ModerateRainShowers -> WeatherTypeParam.ModerateRainShowers
    WeatherType.ViolentRainShowers -> WeatherTypeParam.ViolentRainShowers
    WeatherType.SlightSnowShowers -> WeatherTypeParam.SlightSnowShowers
    WeatherType.HeavySnowShowers -> WeatherTypeParam.HeavySnowShowers
    WeatherType.ModerateThunderstorm -> WeatherTypeParam.ModerateThunderstorm
    WeatherType.SlightHailThunderstorm -> WeatherTypeParam.SlightHailThunderstorm
    WeatherType.HeavyHailThunderstorm -> WeatherTypeParam.HeavyHailThunderstorm
}
