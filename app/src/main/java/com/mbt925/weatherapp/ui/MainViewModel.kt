package com.mbt925.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbt925.weatherapp.core.utils.toStateFlow
import com.mbt925.weatherapp.feature.api.WeatherModel
import com.mbt925.weatherapp.feature.data.mapper.toDomain
import com.mbt925.weatherapp.feature.ui.mapper.toWeatherDataParam
import com.mbt925.weatherapp.feature.ui.models.WeatherScreenParam
import com.mbt925.weatherapp.service.local.UserPreferences
import com.mbt925.weatherapp.service.local.models.TemperatureUnitDao
import com.mbt925.weatherapp.service.local.models.WindSpeedUnitDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val model: WeatherModel,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private var job: Job? = null
    val state = model.state
        .map { it.toWeatherDataParam() }
        .toStateFlow(scope = viewModelScope, initialValue = WeatherScreenParam())

    fun fetchWeatherData() {
        job?.cancel()
        job = viewModelScope.launch {
            val units = userPreferences.getUnits()
            model.fetchWeatherData(
                temperatureUnit = units.temperature.toDomain(),
                windSpeedUnit = units.windSpeed.toDomain(),
            )
        }
    }

    fun toggleTemperatureUnit() {
        viewModelScope.launch {
            val units = userPreferences.getUnits()
            val newUnits = units.copy(
                temperature = when (units.temperature) {
                    TemperatureUnitDao.C -> TemperatureUnitDao.F
                    TemperatureUnitDao.F -> TemperatureUnitDao.C
                }
            )
            userPreferences.setUnits(newUnits)
            fetchWeatherData()
        }
    }

    fun toggleWindSpeedUnit() {
        viewModelScope.launch {
            val units = userPreferences.getUnits()
            val newUnits = units.copy(
                windSpeed = when (units.windSpeed) {
                    WindSpeedUnitDao.Kmh -> WindSpeedUnitDao.Mph
                    WindSpeedUnitDao.Mph -> WindSpeedUnitDao.Kmh
                }
            )
            userPreferences.setUnits(newUnits)
            fetchWeatherData()
        }
    }

}
