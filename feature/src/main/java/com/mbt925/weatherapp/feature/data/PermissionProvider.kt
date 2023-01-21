package com.mbt925.weatherapp.feature.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

interface PermissionProvider {

    fun hasFineLocationAccessPermission(): Boolean
    fun hasCoarseLocationAccessPermission(): Boolean
}

internal class PermissionProviderImpl(
    private val context: Context,
) : PermissionProvider {

    override fun hasFineLocationAccessPermission() = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    override fun hasCoarseLocationAccessPermission() = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED
}
