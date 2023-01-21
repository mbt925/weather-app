package com.mbt925.weatherapp.networking.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    @SerialName("error")
    val error: Boolean,
    @SerialName("reason")
    val reason: String,
)
