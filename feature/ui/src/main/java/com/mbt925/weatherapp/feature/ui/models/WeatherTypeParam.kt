package com.mbt925.weatherapp.feature.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mbt925.weatherapp.feature.ui.R

sealed class WeatherTypeParam(
    @StringRes val weatherDescRes: Int,
    @DrawableRes val iconRes: Int
) {
    object ClearSky : WeatherTypeParam(
        weatherDescRes = R.string.weather_clear_sky,
        iconRes = R.drawable.ic_sunny
    )
    object MainlyClear : WeatherTypeParam(
        weatherDescRes = R.string.weather_mainly_clear,
        iconRes = R.drawable.ic_sunnycloudy
    )
    object PartlyCloudy : WeatherTypeParam(
        weatherDescRes = R.string.weather_partly_cloudy,
        iconRes = R.drawable.ic_cloudy
    )
    object Overcast : WeatherTypeParam(
        weatherDescRes = R.string.weather_overcast,
        iconRes = R.drawable.ic_cloudy
    )
    object Foggy : WeatherTypeParam(
        weatherDescRes = R.string.weather_foggy,
        iconRes = R.drawable.ic_very_cloudy
    )
    object DepositingRimeFog : WeatherTypeParam(
        weatherDescRes = R.string.weather_depositing_rime_fog,
        iconRes = R.drawable.ic_very_cloudy
    )
    object LightDrizzle : WeatherTypeParam(
        weatherDescRes = R.string.weather_light_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    object ModerateDrizzle : WeatherTypeParam(
        weatherDescRes = R.string.weather_moderate_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    object DenseDrizzle : WeatherTypeParam(
        weatherDescRes = R.string.weather_dense_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    object LightFreezingDrizzle : WeatherTypeParam(
        weatherDescRes = R.string.weather_slight_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )
    object DenseFreezingDrizzle : WeatherTypeParam(
        weatherDescRes = R.string.weather_dense_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )
    object SlightRain : WeatherTypeParam(
        weatherDescRes = R.string.weather_slight_rain,
        iconRes = R.drawable.ic_rainy
    )
    object ModerateRain : WeatherTypeParam(
        weatherDescRes = R.string.weather_rainy,
        iconRes = R.drawable.ic_rainy
    )
    object HeavyRain : WeatherTypeParam(
        weatherDescRes = R.string.weather_heavy_rain,
        iconRes = R.drawable.ic_rainy
    )
    object HeavyFreezingRain: WeatherTypeParam(
        weatherDescRes = R.string.weather_heavy_freezing_rain,
        iconRes = R.drawable.ic_snowyrainy
    )
    object SlightSnowFall: WeatherTypeParam(
        weatherDescRes = R.string.weather_slight_snow_fall,
        iconRes = R.drawable.ic_snowy
    )
    object ModerateSnowFall: WeatherTypeParam(
        weatherDescRes = R.string.weather_moderate_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )
    object HeavySnowFall: WeatherTypeParam(
        weatherDescRes = R.string.weather_heavy_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )
    object SnowGrains: WeatherTypeParam(
        weatherDescRes = R.string.weather_snow_grains,
        iconRes = R.drawable.ic_heavysnow
    )
    object SlightRainShowers: WeatherTypeParam(
        weatherDescRes = R.string.weather_slight_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )
    object ModerateRainShowers: WeatherTypeParam(
        weatherDescRes = R.string.weather_moderate_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )
    object ViolentRainShowers: WeatherTypeParam(
        weatherDescRes = R.string.weather_violent_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )
    object SlightSnowShowers: WeatherTypeParam(
        weatherDescRes = R.string.weather_light_snow_showers,
        iconRes = R.drawable.ic_snowy
    )
    object HeavySnowShowers: WeatherTypeParam(
        weatherDescRes = R.string.weather_heavy_snow_showers,
        iconRes = R.drawable.ic_snowy
    )
    object ModerateThunderstorm: WeatherTypeParam(
        weatherDescRes = R.string.weather_moderate_thunderstorm,
        iconRes = R.drawable.ic_thunder
    )
    object SlightHailThunderstorm: WeatherTypeParam(
        weatherDescRes = R.string.weather_thunderstorm_with_slight_hail,
        iconRes = R.drawable.ic_rainythunder
    )
    object HeavyHailThunderstorm: WeatherTypeParam(
        weatherDescRes = R.string.weather_thunderstorm_with_heavy_hail,
        iconRes = R.drawable.ic_rainythunder
    )
}
