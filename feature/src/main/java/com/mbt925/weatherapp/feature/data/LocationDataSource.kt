package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.feature.api.models.LocationAccessResult

interface LocationDataSource {
    suspend fun getCurrentLocation(): LocationAccessResult
}
