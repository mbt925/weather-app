package com.mbt925.weatherapp.feature.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.mbt925.weatherapp.design.theme.SizeM
import com.mbt925.weatherapp.design.theme.SizeS
import com.mbt925.weatherapp.design.theme.SizeXXL
import com.mbt925.weatherapp.design.theme.SizeXXXL
import com.mbt925.weatherapp.design.theme.SizeXXXS
import com.mbt925.weatherapp.design.theme.SizeXXXXL
import com.mbt925.weatherapp.feature.ui.R
import com.mbt925.weatherapp.feature.ui.models.WeatherParam
import com.mbt925.weatherapp.feature.ui.utils.TestTags
import java.time.format.DateTimeFormatter

@Composable
internal fun TodayWeatherGrid(
    modifier: Modifier,
    horizontalPadding: Dp,
    todayWeather: List<WeatherParam>,
    timeFormatter: DateTimeFormatter,
    temperatureUnit: String,
    rows: Int,
) {
    val itemsHeight = SizeXXXXL
    val spacing = SizeXXXS

    Column(
        modifier = modifier.testTag(TestTags.TodayWeatherBox),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(bottom = SizeS),
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
        )

        LazyHorizontalGrid(
            modifier = Modifier
                .height(itemsHeight.times(rows) + spacing.times(rows - 1)),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            rows = GridCells.Fixed(rows),
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            todayWeather.forEach { weather ->
                item(key = weather.dateTime) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.secondary.copy(
                                    alpha = ContentAlpha.disabled,
                                ),
                                shape = MaterialTheme.shapes.small,
                            )
                            .size(SizeXXXL, SizeXXL),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            SizeXXXS, Alignment.CenterVertically),
                    ) {
                        Text(
                            text = timeFormatter.format(weather.dateTime),
                            style = MaterialTheme.typography.body1,
                        )
                        Image(
                            modifier = Modifier
                                .height(SizeM),
                            painter = painterResource(weather.type.iconRes),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                        )
                        Text(
                            text = "${weather.temperature}$temperatureUnit",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
