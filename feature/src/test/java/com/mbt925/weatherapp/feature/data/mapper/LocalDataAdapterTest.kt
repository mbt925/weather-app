package com.mbt925.weatherapp.feature.data.mapper

import com.mbt925.weatherapp.feature.api.models.WeatherDataResult
import com.mbt925.weatherapp.feature.data.weatherData
import com.mbt925.weatherapp.feature.data.weatherDataDao
import com.mbt925.weatherapp.service.local.models.WeatherDataResultDao
import kotlin.test.assertEquals
import org.junit.Test

class LocalDataAdapterTest {

    @Test
    fun dao_to_domain() {
        val data = listOf(
            WeatherDataResult.Success(weatherData),
            WeatherDataResult.Failure.MissingNetwork,
        )
        val daos = listOf(
            WeatherDataResultDao(weatherDataDao),
            null,
        )

        val adapter = getProvider()
        data.forEachIndexed { index, d ->
            val dao = daos[index]
            assertEquals(
                dao,
                adapter.map(d),
            )
        }
    }

    @Test
    fun domain_to_dao() {
        val daos = listOf(
            WeatherDataResultDao(weatherDataDao),
            WeatherDataResultDao(null),
        )
        val data = listOf(
            WeatherDataResult.Success(weatherData),
            null,
        )

        val adapter = getProvider()
        data.forEachIndexed { index, d ->
            val dao = daos[index]
            assertEquals(
                d,
                adapter.map(dao),
            )
        }
    }

    private fun getProvider() = LocalDataAdapterImpl()

}
