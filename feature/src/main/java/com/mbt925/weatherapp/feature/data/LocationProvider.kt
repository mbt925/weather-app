package com.mbt925.weatherapp.feature.data

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.mbt925.weatherapp.feature.api.models.Location
import com.mbt925.weatherapp.feature.api.models.LocationResult
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

interface LocationProvider {
    suspend fun getLastLocation(): LocationResult
}

internal class LocationProviderImpl(
    private val client: FusedLocationProviderClient,
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation() = suspendCancellableCoroutine { cont ->
        client.lastLocation.apply {
            if (isComplete) {
                if (isSuccessful && result != null) {
                    cont.resume(LocationResult.Success(result.toDomain()))
                } else {
                    cont.resume(LocationResult.Failure.NoInternet)
                }
            }
            addOnSuccessListener {
                if (it != null) {
                    cont.resume(LocationResult.Success(result.toDomain()))
                } else {
                    cont.resume(LocationResult.Failure.NoInternet)
                }
            }
            addOnFailureListener {
                cont.resume(LocationResult.Failure.Unknown)
            }
            addOnCanceledListener {
                cont.resume(LocationResult.Failure.Cancelled)
            }
        }
    }

    private fun android.location.Location.toDomain() = Location(
        latitude = latitude,
        longitude = longitude,
    )
}
