package com.mbt925.weatherapp.feature.api.models

sealed interface LocationAccessResult {

    sealed interface Failure : LocationAccessResult {
        object NoFineLocationAccessPermission : Failure
        object NoCourseLocationAccessPermission : Failure
        object GpsDisabled : Failure
    }
}

sealed interface LocationResult : LocationAccessResult {

    data class Success(val location: Location) : LocationResult

    sealed interface Failure : LocationResult, LocationAccessResult.Failure {
        object NoInternet : Failure
        object Cancelled : Failure
        object Unknown : Failure
    }
}
