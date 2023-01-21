package com.mbt925.weatherapp.feature.ui.models

import androidx.annotation.StringRes
import com.mbt925.weatherapp.feature.ui.R

enum class TemperatureUnitParam(@StringRes val key: Int) {
    Celsius(R.string.celsius),
    Fahrenheit(R.string.fahrenheit),
    Unknown(R.string.unknown);
}
