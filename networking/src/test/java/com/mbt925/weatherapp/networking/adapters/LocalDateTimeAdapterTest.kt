package com.mbt925.weatherapp.networking.adapters

import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Test

class LocalDateTimeAdapterTest {

    private val fullTimeStamp = """
        "2020-10-12T14:15"
        """.trimIndent()

    private val fullDateTime =
        LocalDateTime.of(2020, Month.OCTOBER, 12, 14, 15)

    @Test
    fun testDeserialize() {
        val json = buildJson()

        assertEquals(
            fullDateTime,
            json.decodeFromString(fullTimeStamp),
        )
    }

    @Test
    fun testSerialize() {
        val json = buildJson()

        assertEquals(
            fullTimeStamp,
            json.encodeToString<LocalDateTime>(fullDateTime),
        )
    }

    private fun assertEquals(
        date1: LocalDateTime,
        date2: LocalDateTime,
    ) {
        assertEquals(date1.toInstant(ZoneOffset.UTC), date2.toInstant(ZoneOffset.UTC))
    }

    private fun buildJson(): Json {
        return Json {
            serializersModule = SerializersModule {
                contextual(LocalDateTimeSerializer)
            }
        }
    }
}
