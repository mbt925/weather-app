package com.mbt925.weatherapp.service.local

import androidx.datastore.core.DataStore
import com.mbt925.weatherapp.service.local.models.UnitsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.invoke

interface UserPreferences {

    suspend fun getUnits(): UnitsDao
    suspend fun setUnits(data: UnitsDao)
}

internal class UserPreferencesImpl(
    private val dataStore: DataStore<UnitsDao>,
    private val dispatcher: CoroutineDispatcher,
    ) : UserPreferences {

    override suspend fun getUnits() = dataStore.data.first()

    override suspend fun setUnits(data: UnitsDao) = dispatcher {
        dataStore.updateData { data }
        Unit
    }

}
