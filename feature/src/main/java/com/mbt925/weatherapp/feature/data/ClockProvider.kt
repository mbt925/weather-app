package com.mbt925.weatherapp.feature.data

import java.time.Instant

interface ClockProvider {

    fun now() : Instant
}

class ClockProviderImpl : ClockProvider {

    override fun now() : Instant = Instant.now()
}
