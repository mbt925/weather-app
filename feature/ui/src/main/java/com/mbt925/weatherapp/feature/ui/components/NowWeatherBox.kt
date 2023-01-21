package com.mbt925.weatherapp.feature.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mbt925.weatherapp.design.component.IconText
import com.mbt925.weatherapp.design.theme.SizeM
import com.mbt925.weatherapp.design.theme.SizeS
import com.mbt925.weatherapp.design.theme.SizeXXS
import com.mbt925.weatherapp.design.theme.SizeXXXL
import com.mbt925.weatherapp.design.theme.SizeXXXS
import com.mbt925.weatherapp.feature.ui.R
import com.mbt925.weatherapp.feature.ui.models.WeatherParam
import com.mbt925.weatherapp.feature.ui.utils.TestTags
import java.time.format.DateTimeFormatter

@Composable
internal fun NowWeatherBox(
    modifier: Modifier,
    weather: WeatherParam,
    timeFormatter: DateTimeFormatter,
    temperatureUnit: String,
    windSpeedUnit: String,
) {
    val pressureUnit = stringResource(R.string.pressure)
    val humidityUnit = stringResource(R.string.humidity)
    val surfaceShape = MaterialTheme.shapes.medium
    val weatherDesc = stringResource(weather.type.weatherDescRes)

    Surface(
        modifier = modifier
            .testTag(TestTags.NowWeatherBox)
            .padding(bottom = SizeM)
            .shadow(SizeXXS, surfaceShape, clip = false)
            .background(
                color = MaterialTheme.colors.surface,
                shape = surfaceShape,
            )
            .padding(SizeS),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = SizeXXS)
                        .weight(1f),
                    text = weatherDesc,
                    style = MaterialTheme.typography.h6,
                )
                IconText(
                    text = timeFormatter.format(weather.dateTime),
                    iconResId = R.drawable.ic_clock,
                    onClick = null,
                )
            }

            Spacer(Modifier.height(SizeXXS))

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(SizeXXXL),
                painter = painterResource(weather.type.iconRes),
                contentDescription = weatherDesc,
                contentScale = ContentScale.FillHeight
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "${weather.temperature}$temperatureUnit",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(SizeXXS))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconText(
                    text = "${weather.pressure}$pressureUnit",
                    iconResId = R.drawable.ic_pressure,
                    onClick = null,
                )
                Spacer(Modifier.width(SizeXXXS))
                IconText(
                    text = "${weather.humidity}$humidityUnit",
                    iconResId = R.drawable.ic_drop,
                    onClick = null,
                )
                Spacer(Modifier.width(SizeXXXS))
                IconText(
                    text = "${weather.windSpeed}$windSpeedUnit",
                    iconResId = R.drawable.ic_wind,
                    onClick = null,
                )
            }
        }
    }
}
