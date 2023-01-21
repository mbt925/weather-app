package com.mbt925.weatherapp.feature.data

import com.mbt925.weatherapp.core.test.TaskExecutorRule
import com.mbt925.weatherapp.feature.api.models.Location
import com.mbt925.weatherapp.feature.api.models.LocationAccessResult.Failure
import com.mbt925.weatherapp.feature.api.models.LocationResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class LocationDataSourceImplTest {

    @get:Rule
    val rule = TaskExecutorRule()

    private val locationProvider = mockk<LocationProvider>()
    private val permissionProvider = mockk<PermissionProvider> {
        every { hasCoarseLocationAccessPermission() } returns false
        every { hasFineLocationAccessPermission() } returns false
    }
    private val gpsProvider = mockk<GpsProvider> {
        every { isGpsEnabled() } returns false
    }

    @Test
    fun onNoCourseLocationAccessPermission_returnsNoCourseLocationAccessPermissionFailure() =
        rule.runTest {
            every { permissionProvider.hasFineLocationAccessPermission() } returns true
            val expected = Failure.NoCourseLocationAccessPermission

            assertEquals(expected, getDataSource().getCurrentLocation())
        }

    @Test
    fun onNoFineLocationAccessPermission_returnsNoFineLocationAccessPermissionFailure() =
        rule.runTest {
            every { permissionProvider.hasCoarseLocationAccessPermission() } returns true
            val expected = Failure.NoFineLocationAccessPermission

            assertEquals(expected, getDataSource().getCurrentLocation())
        }

    @Test
    fun onGpsDisabled_returnsGpsDisabledFailure() =
        rule.runTest {
            every { permissionProvider.hasFineLocationAccessPermission() } returns true
            every { permissionProvider.hasCoarseLocationAccessPermission() } returns true
            val expected = Failure.GpsDisabled

            assertEquals(expected, getDataSource().getCurrentLocation())
        }

    @Test
    fun onSuccessfulLastLocation_returnsLocation() =
        rule.runTest {
            every { permissionProvider.hasFineLocationAccessPermission() } returns true
            every { permissionProvider.hasCoarseLocationAccessPermission() } returns true
            every { gpsProvider.isGpsEnabled() } returns true
            val location = Location(1.0, 2.0)
            val expected = LocationResult.Success(location)
            coEvery { locationProvider.getLastLocation() } returns expected

            assertEquals(expected, getDataSource().getCurrentLocation())
        }

    @Test
    fun onFailedLastLocation_returnsFailure() =
        rule.runTest {
            every { permissionProvider.hasFineLocationAccessPermission() } returns true
            every { permissionProvider.hasCoarseLocationAccessPermission() } returns true
            every { gpsProvider.isGpsEnabled() } returns true
            val expected = LocationResult.Failure.NoInternet
            coEvery { locationProvider.getLastLocation() } returns expected

            assertEquals(expected, getDataSource().getCurrentLocation())
        }

    private fun getDataSource() = LocationDataSourceImpl(
        locationProvider = locationProvider,
        permissionProvider = permissionProvider,
        gpsProvider = gpsProvider,
    )

}
