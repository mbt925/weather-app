package com.mbt925.weatherapp.feature.api.models

sealed interface WeatherDataResult {

    data class Success(val data: WeatherData) : WeatherDataResult

    sealed interface Failure : WeatherDataResult {
        data class MissingLocation(val error: LocationAccessResult.Failure) : Failure
        object MissingNetwork : Failure
        data class UnknownError(val message: String) : Failure
    }
}
