package com.mbt925.weatherapp.feature.ui.models

import androidx.annotation.StringRes
import com.mbt925.weatherapp.feature.ui.R

enum class WindSpeedUnitParam(@StringRes val key: Int) {
    KilometresPerHour(R.string.kmh),
    MilesPerHour(R.string.mph),
    Unknown(R.string.unknown);
}
