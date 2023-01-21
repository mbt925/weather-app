package com.mbt925.weatherapp.feature.di

import com.google.android.gms.location.LocationServices
import com.mbt925.weatherapp.feature.api.WeatherModel
import com.mbt925.weatherapp.feature.api.WeatherRepository
import com.mbt925.weatherapp.feature.data.ClockProvider
import com.mbt925.weatherapp.feature.data.ClockProviderImpl
import com.mbt925.weatherapp.feature.data.GpsProvider
import com.mbt925.weatherapp.feature.data.GpsProviderImpl
import com.mbt925.weatherapp.feature.data.LocationDataSource
import com.mbt925.weatherapp.feature.data.LocationDataSourceImpl
import com.mbt925.weatherapp.feature.data.LocationProvider
import com.mbt925.weatherapp.feature.data.LocationProviderImpl
import com.mbt925.weatherapp.feature.data.PermissionProvider
import com.mbt925.weatherapp.feature.data.PermissionProviderImpl
import com.mbt925.weatherapp.feature.data.WeatherRemoteDataSource
import com.mbt925.weatherapp.feature.data.WeatherRemoteDataSourceImpl
import com.mbt925.weatherapp.feature.data.WeatherRepositoryImpl
import com.mbt925.weatherapp.feature.data.mapper.LocalDataAdapter
import com.mbt925.weatherapp.feature.data.mapper.LocalDataAdapterImpl
import com.mbt925.weatherapp.feature.data.mapper.RemoteDataAdapter
import com.mbt925.weatherapp.feature.data.mapper.RemoteDataAdapterImpl
import com.mbt925.weatherapp.feature.domain.WeatherContextImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val featureModule = module {

    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            locationDataSource = get(),
            remoteDataAdapter = get(),
            localDataAdapter = get(),
        )
    }
    factory<WeatherRemoteDataSource> {
        WeatherRemoteDataSourceImpl(
            service = get(),
            dispatcher = Dispatchers.IO,
        )
    }

    factory<PermissionProvider> { PermissionProviderImpl(androidContext()) }
    factory<GpsProvider> { GpsProviderImpl(androidContext()) }
    factory<LocationProvider> {
        LocationProviderImpl(LocationServices.getFusedLocationProviderClient(androidContext()))
    }
    single<ClockProvider> { ClockProviderImpl() }
    factory<LocalDataAdapter> { LocalDataAdapterImpl() }
    factory<RemoteDataAdapter> { RemoteDataAdapterImpl(clockProvider = get()) }

    factory<LocationDataSource> {
        LocationDataSourceImpl(
            locationProvider = get(),
            permissionProvider = get(),
            gpsProvider = get()
        )
    }

    factory<WeatherModel> { WeatherContextImpl(weatherRepository = get()) }

}
