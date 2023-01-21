package com.mbt925.weatherapp.networking

import com.mbt925.weatherapp.networking.models.ApiResponse
import java.net.HttpRetryException
import okio.IOException
import retrofit2.Call

fun <T> Call<T>.executeApi(): ApiResponse<T> {
    try {
        val response = execute()
        return if (response.isSuccessful) {
            return ApiResponse.success(
                data = response.body()
            )
        } else {
            response.errorBody()?.let { errorBody ->
                ApiResponse.failure(
                    statusCode = response.code(),
                    errorBody = errorBody.string(),
                )
            } ?: ApiResponse.failure(
                error = IllegalStateException(
                    "Unexpected error ${response.code()} with no error body"
                )
            )

        }
    } catch (exception: IOException) {
        return ApiResponse.failure(error = exception)
    } catch (exception: HttpRetryException) {
        return ApiResponse.failure(error = exception)
    }
}
