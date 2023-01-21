package com.mbt925.weatherapp.feature.domain

import com.mbt925.weatherapp.core.domain.ContextState
import com.mbt925.weatherapp.core.domain.UseCase
import com.mbt925.weatherapp.feature.api.WeatherRepository
import com.mbt925.weatherapp.feature.api.WeatherState
import com.mbt925.weatherapp.feature.api.models.TemperatureUnit
import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.api.models.WindSpeedUnit
import java.time.OffsetDateTime

internal class FetchWeatherDataUseCase(
    private val weatherRepository: WeatherRepository,
    private val temperatureUnit: TemperatureUnit,
    private val windSpeedUnit: WindSpeedUnit,
) : UseCase<WeatherState> {

    sealed interface Effect {
        object Loading : Effect
        data class Success(
            val offline: Boolean,
            val value: WeatherDataResult.Success,
        ) : Effect

        data class Failure(val error: WeatherDataResult.Failure) : Effect
    }

    private fun ContextState<WeatherState>.reducer(effect: Effect) {
        when (effect) {
            Effect.Loading -> reduce {
                it.copy(
                    loading = true,
                )
            }
            is Effect.Failure -> reduce {
                it.copy(
                    loading = false,
                    failure = effect.error,
                    offline = true,
                )
            }
            is Effect.Success -> reduce {
                it.copy(
                    loading = false,
                    offline = effect.offline,
                    temperatureUnit = effect.value.data.temperatureUnit,
                    windSpeedUnit = effect.value.data.windSpeedUnit,
                    weather = effect.value,
                    failure = null,
                )
            }
        }
    }

    override suspend fun invoke(context: ContextState<WeatherState>) = with(context) {
        reducer(Effect.Loading)

        val dataAccess = weatherRepository.getData(
            temperatureUnit = temperatureUnit,
            windSpeedUnit = windSpeedUnit,
        ).getAndFetch()

        dataAccess.collect { result ->
            when (result) {
                is WeatherDataResult.Success -> {
                    val offline = result.data.now.dateTime.hour != OffsetDateTime.now().hour
                    reducer(Effect.Success(
                        offline = offline,
                        value = result,
                    ))
                }
                is WeatherDataResult.Failure -> reducer(Effect.Failure(result))
            }
        }
    }
}
