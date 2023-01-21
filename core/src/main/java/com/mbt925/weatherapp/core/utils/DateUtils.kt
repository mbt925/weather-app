package com.mbt925.weatherapp.core.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

fun LocalDateTime.toOffsetDateTime(): OffsetDateTime =
    atZone(ZoneId.systemDefault()).toOffsetDateTime()

fun LocalDateTime.toInstant(): Instant = toOffsetDateTime().toInstant()

fun Instant.toOffsetDateTime(): OffsetDateTime =
    atZone(ZoneId.systemDefault()).toOffsetDateTime()
