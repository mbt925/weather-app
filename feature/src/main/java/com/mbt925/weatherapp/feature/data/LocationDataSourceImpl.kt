package com.mbt925.weatherapp.feature.data

import android.annotation.SuppressLint
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult

class LocationDataSourceImpl(
    private val locationProvider: LocationProvider,
    private val permissionProvider: PermissionProvider,
    private val gpsProvider: GpsProvider,
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationAccessResult {
        if (!permissionProvider.hasFineLocationAccessPermission()) {
            return LocationAccessResult.Failure.NoFineLocationAccessPermission
        }
        if (!permissionProvider.hasCoarseLocationAccessPermission()) {
            return LocationAccessResult.Failure.NoCourseLocationAccessPermission
        }
        if (!gpsProvider.isGpsEnabled()) return LocationAccessResult.Failure.GpsDisabled

        return locationProvider.getLastLocation()
    }
}
