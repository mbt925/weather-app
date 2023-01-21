package com.mbt925.weatherapp.service.local

import androidx.datastore.core.DataStore
import com.mbt925.weatherapp.service.local.models.WeatherDataResultDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.invoke

interface WeatherLocalDataSource {

    suspend fun getWeatherData(): WeatherDataResultDao?
    suspend fun setWeatherData(data: WeatherDataResultDao)
}

internal class WeatherLocalDataSourceImpl(
    private val dataStore: DataStore<WeatherDataResultDao>,
    private val dispatcher: CoroutineDispatcher,
    ) : WeatherLocalDataSource {

    override suspend fun getWeatherData(): WeatherDataResultDao? = dispatcher {
        dataStore.data.firstOrNull()
    }

    override suspend fun setWeatherData(data: WeatherDataResultDao) = dispatcher {
        dataStore.updateData { data }
        Unit
    }

}
