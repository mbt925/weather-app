package com.mbt925.weatherapp.service.local

import android.content.Context
import androidx.datastore.dataStore

val Context.weatherDataResultDataSource by dataStore(
    fileName = "cache",
    serializer = WeatherDataResultDaoSerializer,
)

val Context.userPreferences by dataStore(
    fileName = "user_preferences",
    serializer = UnitsDaoSerializer,
)
