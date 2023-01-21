package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.core.test.TaskExecutorRule
import com.mbt925.weatherapp.feature.api.models.Location
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult
import com.mbt925.weatherapp.feature.api.models.LocationResult
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.data.mapper.LocalDataAdapterImpl
import com.mbt925.weatherapp.feature.data.mapper.RemoteDataAdapterImpl
import com.mbt925.weatherapp.feature.data.mapper.toDto
import com.mbt925.weatherapp.networking.models.ApiResponse
import com.mbt925.weatherapp.service.local.WeatherLocalDataSource
import com.mbt925.weatherapp.service.local.models.WeatherDataResultDao
import com.mbt925.weatherapp.service.remote.LatitudeWrapper
import com.mbt925.weatherapp.service.remote.LongitudeWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import java.time.Instant
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test

class WeatherRepositoryImplTest {

    @get:Rule
    val rule = TaskExecutorRule()

    private val localDataSource = mockk<WeatherLocalDataSource> {
        coEvery { getWeatherData() } returns WeatherDataResultDao(null)
        coEvery { setWeatherData(any()) } just runs
    }
    private val remoteDataSource = mockk<WeatherRemoteDataSource> {
        coEvery {
            fetchWeatherData(any(), any(), any(), any())
        } returns ApiResponse.failure(Exception())
    }
    private val locationDataSource = mockk<LocationDataSource> {
        coEvery { getCurrentLocation() } returns LocationAccessResult.Failure.GpsDisabled
    }
    private val clockProvider = mockk<ClockProvider> {
        every { now() } returns Instant.now()
    }
    private val localDataAdapter = LocalDataAdapterImpl()
    private val remoteDataAdapter = RemoteDataAdapterImpl(clockProvider)

    @Test
    fun onGetData_callsRemote_getsFromCache() = rule.runTest {
        val lat = 12.0
        val lng = 14.0
        coEvery { locationDataSource.getCurrentLocation() } returns LocationResult.Success(
            location = Location(lat, lng)
        )
        val tempUnit = TemperatureUnit.Celsius
        val speedUnit = WindSpeedUnit.MilesPerHour
        getRepo().getData(tempUnit, speedUnit).getAndFetch().first()

        coVerify(exactly = 1) {
            remoteDataSource.fetchWeatherData(
                LatitudeWrapper(lat),
                LongitudeWrapper(lng),
                temperatureUnit = tempUnit.toDto(),
                windSpeedUnit = speedUnit.toDto(),
            )
        }

        coVerify(exactly = 1) { localDataSource.getWeatherData() }
    }

    @Test
    fun onGetData_getsFromCache() = rule.runTest {
        val lat = 12.0
        val lng = 14.0
        coEvery { locationDataSource.getCurrentLocation() } returns LocationResult.Success(
            location = Location(lat, lng)
        )
        coEvery {
            remoteDataSource.fetchWeatherData(any(), any(), any(), any())
        } returns ApiResponse.success(weatherDataDto)
        coEvery { localDataSource.getWeatherData() } returns WeatherDataResultDao(weatherDataDao)

        val tempUnit = weatherData.temperatureUnit
        val speedUnit = weatherData.windSpeedUnit
        val actual = getRepo().getData(tempUnit, speedUnit).getAndFetch().first()
        val expected = WeatherDataResult.Success(weatherData)

        coVerify(exactly = 1) { localDataSource.getWeatherData() }
        assertEquals(expected, actual)
    }

    @Test
    fun onGetData_cachesData() = rule.runTest {
        val lat = 12.0
        val lng = 14.0
        coEvery { locationDataSource.getCurrentLocation() } returns LocationResult.Success(
            location = Location(lat, lng)
        )
        coEvery {
            remoteDataSource.fetchWeatherData(any(), any(), any(), any())
        } returns ApiResponse.success(weatherDataDto)

        val tempUnit = weatherData.temperatureUnit
        val speedUnit = weatherData.windSpeedUnit
        getRepo().getData(tempUnit, speedUnit).getAndFetch().collect()
        val expected = WeatherDataResultDao(weatherDataDao)

        coVerify(exactly = 1) { localDataSource.setWeatherData(expected) }
    }

    @Test
    fun onFailure_notCache() = rule.runTest {
        val lat = 12.0
        val lng = 14.0
        coEvery { locationDataSource.getCurrentLocation() } returns LocationResult.Success(
            location = Location(lat, lng)
        )
        coEvery {
            remoteDataSource.fetchWeatherData(any(), any(), any(), any())
        } returns ApiResponse.failure(Exception("error"))

        val tempUnit = weatherData.temperatureUnit
        val speedUnit = weatherData.windSpeedUnit
        val actual = getRepo().getData(tempUnit, speedUnit).getAndFetch().first()
        val expected = WeatherDataResult.Failure.UnknownError("error")

        coVerify(exactly = 0) { localDataSource.setWeatherData(any()) }
        assertEquals(actual, expected)
    }

    private fun getRepo() = WeatherRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        locationDataSource = locationDataSource,
        localDataAdapter = localDataAdapter,
        remoteDataAdapter = remoteDataAdapter,
    )
}
