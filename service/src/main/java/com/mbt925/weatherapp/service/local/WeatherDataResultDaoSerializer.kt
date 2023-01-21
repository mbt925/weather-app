package com.mbt925.weatherapp.service.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mbt925.weatherapp.service.local.models.WeatherDataResultDao
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object WeatherDataResultDaoSerializer : Serializer<WeatherDataResultDao> {

    override val defaultValue = WeatherDataResultDao()

    override suspend fun readFrom(input: InputStream): WeatherDataResultDao {
        try {
            return Json.decodeFromString(
                WeatherDataResultDao.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read cache data store", serialization)
        }
    }

    override suspend fun writeTo(t: WeatherDataResultDao, output: OutputStream) {
        output.write(
            Json.encodeToString(WeatherDataResultDao.serializer(), t)
                .encodeToByteArray()
        )
    }
}
