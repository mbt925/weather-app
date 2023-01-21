package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.core.test.TaskExecutorRule
import com.mbt925.weatherapp.service.remote.LatitudeWrapper
import com.mbt925.weatherapp.service.remote.LongitudeWrapper
import com.mbt925.weatherapp.service.remote.TemperatureUnitWrapper
import com.mbt925.weatherapp.service.remote.WeatherService
import com.mbt925.weatherapp.service.remote.WindSpeedUnitWrapper
import com.mbt925.weatherapp.service.remote.model.TemperatureUnitDto
import com.mbt925.weatherapp.service.remote.model.WindSpeedUnitDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class WeatherRemoteDataSourceImplTest {

    @get:Rule
    val rule = TaskExecutorRule()

    private val service = mockk<WeatherService> {
        every { getWeatherData(any(), any(), any(), any()) } returns mockCall(weatherDataDto)
    }

    @Test
    fun onFetchData_callsService() = rule.runTest {
        val lat = LatitudeWrapper(1.0)
        val lng = LongitudeWrapper(2.0)
        val tempUnit = TemperatureUnitDto.C
        val speedUnit = WindSpeedUnitDto.Mph
        getDataSource().fetchWeatherData(lat, lng, tempUnit, speedUnit)

        coVerify {
            service.getWeatherData(
                latitude = lat,
                longitude = lng,
                tempUnit = TemperatureUnitWrapper(tempUnit.key),
                windSpeedUnit = WindSpeedUnitWrapper(speedUnit.key),
            )
        }
    }
    private fun <T> mockCall(data: T): retrofit2.Call<T> = mockk {
        coEvery { execute() } returns Response.success(data)
    }

    private fun getDataSource() = WeatherRemoteDataSourceImpl(
        service = service,
        dispatcher = rule.dispatcher,
    )

}
