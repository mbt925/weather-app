package com.mbt925.weatherapp.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.mbt925.weatherapp.design.platform.WindowHeightSizeClass
import com.mbt925.weatherapp.design.platform.WindowSizeClass
import com.mbt925.weatherapp.design.platform.WindowWidthSizeClass
import com.mbt925.weatherapp.design.theme.WeatherAppTheme
import com.mbt925.weatherapp.feature.ui.WeatherScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.fetchWeatherData()
        }

        if (savedInstanceState == null) {
            checkPermission()
        }

        setContent {
            val windowSizeClass = WindowSizeClass.calculateFrom(activity = this)
            val state = viewModel.state.collectAsState().value.copy(
                todayRows = when (windowSizeClass.heightSizeClass) {
                    WindowHeightSizeClass.Compact -> 1
                    WindowHeightSizeClass.Medium -> 2
                    WindowHeightSizeClass.Extended -> 3
                },
                hasVerticalLayout = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Extended,
            )

            WeatherAppTheme {
                WeatherScreen(
                    param = state,
                    onToggleTemperatureUnit = viewModel::toggleTemperatureUnit,
                    onToggleWindSpeedUnit = viewModel::toggleWindSpeedUnit,
                    onRetry = viewModel::fetchWeatherData,
                    onAskForPermission = ::checkPermission,
                )
            }
        }
    }

    private fun checkPermission() {
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))
    }

}
