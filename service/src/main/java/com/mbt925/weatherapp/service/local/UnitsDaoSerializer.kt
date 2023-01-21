package com.mbt925.weatherapp.service.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mbt925.weatherapp.service.local.models.UnitsDao
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object UnitsDaoSerializer : Serializer<UnitsDao> {

    override val defaultValue = UnitsDao()

    override suspend fun readFrom(input: InputStream): UnitsDao {
        try {
            return Json.decodeFromString(
                UnitsDao.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read user preferences", serialization)
        }
    }

    override suspend fun writeTo(t: UnitsDao, output: OutputStream) {
        output.write(
            Json.encodeToString(UnitsDao.serializer(), t)
                .encodeToByteArray()
        )
    }
}
