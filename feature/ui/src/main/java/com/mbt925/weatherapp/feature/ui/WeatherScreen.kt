package com.mbt925.weatherapp.feature.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.mbt925.weatherapp.design.component.Spinner
import com.mbt925.weatherapp.design.overlay.DefaultSnackBar
import com.mbt925.weatherapp.design.preview.ScreenPreviews
import com.mbt925.weatherapp.design.theme.SizeXXXXL
import com.mbt925.weatherapp.design.theme.WeatherAppTheme
import com.mbt925.weatherapp.feature.ui.components.NowWeatherBox
import com.mbt925.weatherapp.feature.ui.components.TodayWeatherGrid
import com.mbt925.weatherapp.feature.ui.components.UnitsToolbar
import com.mbt925.weatherapp.feature.ui.components.WeatherScreenLayout
import com.mbt925.weatherapp.feature.ui.models.FailureParam
import com.mbt925.weatherapp.feature.ui.models.FailureParam.PermissionDenied
import com.mbt925.weatherapp.feature.ui.models.FailureParam.UnknownError
import com.mbt925.weatherapp.feature.ui.models.WeatherScreenParam
import com.mbt925.weatherapp.feature.ui.models.WeatherScreenParamProvider
import com.mbt925.weatherapp.feature.ui.utils.TestTags
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Composable
fun WeatherScreen(
    param: WeatherScreenParam,
    onRetry: () -> Unit,
    onAskForPermission: () -> Unit,
    onToggleTemperatureUnit: () -> Unit,
    onToggleWindSpeedUnit: () -> Unit,
) {
    val temperatureUnit = stringResource(param.temperatureUnit.key)
    val windSpeedUnit = stringResource(param.windSpeedUnit.key)

    val timeFormatter: DateTimeFormatter = remember {
        DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter()
    }

    val scaffoldState = rememberScaffoldState()
    HandleErrors(
        scaffoldState = scaffoldState,
        failureParam = param.failureParam,
    )

    val toolbar: @Composable (Boolean) -> Unit = { isHorizontal ->
        UnitsToolbar(
            temperatureUnit = param.temperatureUnit,
            windSpeedUnit = param.windSpeedUnit,
            isHorizontal = isHorizontal,
            offline = param.offline,
            onToggleTemperatureUnit = onToggleTemperatureUnit,
            onToggleWindSpeedUnit = onToggleWindSpeedUnit,
        )
    }

    val nowWeather: @Composable (Modifier) -> Unit = { modifier ->
        if (param.isLoading) {
            LoadingBox()
        } else if (param.nowWeather != null) {
            NowWeatherBox(
                modifier = modifier,
                weather = param.nowWeather,
                timeFormatter = timeFormatter,
                temperatureUnit = temperatureUnit,
                windSpeedUnit = windSpeedUnit,
            )
        }
    }

    val todayWeather: @Composable (Modifier, Dp) -> Unit = { modifier, horizontalPadding ->
        if (param.todayWeather != null && param.todayWeather.isNotEmpty()) {
            TodayWeatherGrid(
                modifier = modifier,
                horizontalPadding = horizontalPadding,
                todayWeather = param.todayWeather,
                timeFormatter = timeFormatter,
                temperatureUnit = temperatureUnit,
                rows = param.todayRows,
            )
        }
    }

    val snackBar: @Composable (Modifier, Modifier) -> Unit = { modifier, contentModifier ->
        DefaultSnackBar(
            modifier = modifier,
            contentModifier = contentModifier.navigationBarsPadding(),
            hostState = scaffoldState.snackbarHostState,
            onAction = {
                when (param.failureParam) {
                    is PermissionDenied -> onAskForPermission()
                    else -> onRetry()
                }
            },
        )
    }

    WeatherScreenLayout(
        isVertical = param.hasVerticalLayout,
        toolbar = toolbar,
        nowWeather = nowWeather,
        todayWeather = todayWeather,
        snackBar = snackBar,
    )
}

@Composable
private fun LoadingBox() {
    Box(
        modifier = Modifier
            .testTag(TestTags.LoadingWeatherData)
            .fillMaxWidth()
            .height(SizeXXXXL),
    ) {
        Spinner(
            modifier = Modifier.align(Center),
        )
    }
}

@Composable
private fun HandleErrors(
    scaffoldState: ScaffoldState,
    failureParam: FailureParam?,
) {
    if (failureParam != null) {
        val message = when (failureParam) {
            is UnknownError -> stringResource(failureParam.message, failureParam.reason)
            else -> stringResource(failureParam.message)
        }
        val actionLabel = stringResource(R.string.retry)

        LaunchedEffect(failureParam) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Indefinite,
            )
        }
    } else {
        LaunchedEffect(Unit) {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}

@ScreenPreviews
@Composable
private fun WeatherScreenPreview(
    @PreviewParameter(WeatherScreenParamProvider::class) param: WeatherScreenParam,
) {
    WeatherAppTheme {
        WeatherScreen(
            param = param,
            onRetry = {},
            onAskForPermission = {},
            onToggleTemperatureUnit = {},
            onToggleWindSpeedUnit = {},
        )
    }
}
