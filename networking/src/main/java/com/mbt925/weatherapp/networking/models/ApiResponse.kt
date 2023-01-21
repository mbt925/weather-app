package com.mbt925.weatherapp.networking.models

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed interface ApiResponse<out T> {

    data class Success<T>(val data: T?) : ApiResponse<T>

    sealed interface Failure : ApiResponse<Nothing> {

        data class Known(
            val statusCode: Int,
            val message: String,
        ) : Failure

        data class Unknown(val error: Throwable) : Failure

    }

    companion object {
        fun <T> success(data: T?) = Success(data)

        fun failure(
            statusCode: Int,
            errorBody: String,
        ): ApiResponse<Nothing> = Failure.Known(
            statusCode = statusCode,
            message = Json.decodeFromString<ApiError>(errorBody).reason,
        )

        fun failure(
            error: Throwable,
        ): ApiResponse<Nothing> = Failure.Unknown(error)
    }
}
