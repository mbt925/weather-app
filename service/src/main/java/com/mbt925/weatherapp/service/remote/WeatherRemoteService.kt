package com.mbt925.weatherapp.service.remote

import com.mbt925.weatherapp.service.remote.model.WeatherDataDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    companion object {
        private const val KEY_PARAM_LATITUDE = "latitude"
        private const val KEY_PARAM_LONGITUDE = "longitude"
        private const val KEY_PARAM_TEMP_UNIT = "temperature_unit"
        private const val KEY_PARAM_WIND_SPEED_UNIT = "windspeed_unit"
    }

    @GET("forecast?hourly=temperature_2m,relativehumidity_2m,windspeed_10m,pressure_msl,weathercode")
    fun getWeatherData(
        @Query(KEY_PARAM_LATITUDE) latitude: LatitudeWrapper,
        @Query(KEY_PARAM_LONGITUDE) longitude: LongitudeWrapper,
        @Query(KEY_PARAM_TEMP_UNIT) tempUnit: TemperatureUnitWrapper,
        @Query(KEY_PARAM_WIND_SPEED_UNIT) windSpeedUnit: WindSpeedUnitWrapper,
    ): Call<WeatherDataDto>

}

@JvmInline
value class LatitudeWrapper(val value: Double)

@JvmInline
value class LongitudeWrapper(val value: Double)

@JvmInline
value class TemperatureUnitWrapper(val value: String)

@JvmInline
value class WindSpeedUnitWrapper(val value: String)
