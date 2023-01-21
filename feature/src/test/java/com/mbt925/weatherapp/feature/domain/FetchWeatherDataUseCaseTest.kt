package com.mbt925.weatherapp.feature.domain

import com.mbt925.weatherapp.core.data.DataAccess
import com.mbt925.weatherapp.core.data.invoke
import com.mbt925.weatherapp.core.domain.Context
import com.mbt925.weatherapp.core.test.TaskExecutorRule
import com.mbt925.weatherapp.core.test.testLaunch
import com.mbt925.weatherapp.feature.api.WeatherRepository
import com.mbt925.weatherapp.feature.api.WeatherState
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult.Failure
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult.Success
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import com.mbt925.weatherapp.feature.data.weatherData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import org.junit.Test

class FetchWeatherDataUseCaseTest {

    @get:Rule
    val rule = TaskExecutorRule()

    private val repository = mockk<WeatherRepository> {
        coEvery {
            getData(any(), any())
        } returns DataAccess { Failure.MissingNetwork }
    }
    private val context = Context(WeatherState())

    @Test
    fun onInvoke_callsRepository() = rule.runTest {
        val temperatureUnit = TemperatureUnit.Celsius
        val windSpeedUnit = WindSpeedUnit.KilometresPerHour
        getUseCase(temperatureUnit, windSpeedUnit).invoke(context)

        coVerify { repository.getData(temperatureUnit, windSpeedUnit) }
    }

    @Test
    fun invoke_onSuccess_emitsLoading_thenSuccess() = rule.runTest {
        verifyState(
            expectedResult = Success(data = weatherData),
            offline = false,
        )
    }

    @Test
    fun invoke_onSuccess_emitsLoading_thenSuccess_offlineMode() = rule.runTest {
        verifyState(
            expectedResult = Success(
                data = weatherData.copy(now = weatherData.now.copy(OffsetDateTime.MIN)),
            ),
            offline = true,
        )
    }

    @Test
    fun invoke_onMissingNetworkFailure_emitsLoading_thenFailure() = rule.runTest {
        verifyState(
            expectedResult = Failure.MissingNetwork,
            offline = true,
        )
    }

    @Test
    fun invoke_onMissingLocationFailure_emitsLoading_thenFailure() = rule.runTest {
        verifyState(
            expectedResult = Failure.MissingLocation(LocationAccessResult.Failure.GpsDisabled),
            offline = true,
        )
    }

    @Test
    fun invoke_onUnknownErrorFailure_emitsLoading_thenFailure() = rule.runTest {
        verifyState(
            expectedResult = Failure.UnknownError("message"),
            offline = true,
        )
    }

    private suspend fun TestScope.verifyState(
        expectedResult: WeatherDataResult,
        offline: Boolean,
    ) {
        coEvery {
            repository.getData(any(), any())
        } returns DataAccess { expectedResult }

        val stateChanges = mutableListOf<WeatherState>()
        val job = testLaunch { context.state.toCollection(stateChanges) }

        getUseCase().invoke(context)

        job.cancel()
        stateChanges[1].let {
            assertTrue(it.loading)
            assertNull(it.weather)
            assertNull(it.failure)
        }
        stateChanges[2].let {
            assertFalse(it.loading)
            when (expectedResult) {
                is Success -> {
                    assertEquals(expectedResult, it.weather)
                    assertNull(it.failure)
                    assertEquals(expectedResult.data.temperatureUnit, it.temperatureUnit)
                    assertEquals(expectedResult.data.windSpeedUnit, it.windSpeedUnit)
                }
                is Failure -> {
                    assertNull(it.weather)
                    assertEquals(expectedResult, it.failure)
                }
            }
            assertEquals(offline, it.offline)
        }
    }

    private fun getUseCase(
        temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
        windSpeedUnit: WindSpeedUnit = WindSpeedUnit.KilometresPerHour,
    ) = FetchWeatherDataUseCase(
        weatherRepository = repository,
        temperatureUnit = temperatureUnit,
        windSpeedUnit = windSpeedUnit,
    )

}
