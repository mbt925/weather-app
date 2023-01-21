package com.mbt925.weatherapp.feature.data

import android.content.Context
import android.location.LocationManager

interface GpsProvider {
    fun isGpsEnabled(): Boolean
}

internal class GpsProviderImpl(
    private val context: Context,
) : GpsProvider {
    override fun isGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

}
