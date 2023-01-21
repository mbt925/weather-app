package com.mbt925.weatherapp.feature.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.mbt925.weatherapp.design.component.IconText
import com.mbt925.weatherapp.design.theme.SizeS
import com.mbt925.weatherapp.design.theme.SizeXXS
import com.mbt925.weatherapp.feature.ui.R
import com.mbt925.weatherapp.feature.ui.models.TemperatureUnitParam
import com.mbt925.weatherapp.feature.ui.models.WindSpeedUnitParam
import com.mbt925.weatherapp.feature.ui.utils.TestTags

@Composable
internal fun UnitsToolbar(
    temperatureUnit: TemperatureUnitParam,
    windSpeedUnit: WindSpeedUnitParam,
    isHorizontal: Boolean,
    offline: Boolean,
    onToggleTemperatureUnit: () -> Unit,
    onToggleWindSpeedUnit: () -> Unit,
) {
    val tempButton: @Composable () -> Unit = {
        IconText(
            modifier = Modifier.padding(SizeXXS),
            text = stringResource(temperatureUnit.key),
            iconResId = R.drawable.ic_temperature,
            onClick = onToggleTemperatureUnit,
        )
    }
    val windSpeedButton: @Composable () -> Unit = {
        IconText(
            modifier = Modifier.padding(SizeXXS),
            text = stringResource(windSpeedUnit.key),
            iconResId = R.drawable.ic_wind,
            onClick = onToggleWindSpeedUnit,
        )
    }
    val connectivity: @Composable () -> Unit = {
        IconText(
            modifier = Modifier.padding(SizeXXS),
            text = stringResource(if (offline) R.string.offline else R.string.online),
            iconResId = if (offline) R.drawable.ic_offline else R.drawable.ic_online,
            onClick = null,
        )
    }

    if (isHorizontal) {
        Row(
            modifier = Modifier
                .testTag(TestTags.HorizontalToolbar)
                .fillMaxWidth()
                .padding(vertical = SizeS),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tempButton()
            connectivity()
            windSpeedButton()
        }
    } else {
        Column(
            modifier = Modifier
                .testTag(TestTags.VerticalToolbar)
                .fillMaxHeight()
                .padding(horizontal = SizeS),
            verticalArrangement = Arrangement.spacedBy(SizeS),
            horizontalAlignment = Alignment.Start,
        ) {
            tempButton()
            windSpeedButton()
            connectivity()
        }
    }
}
