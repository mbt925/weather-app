package com.mbt925.weatherapp.feature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mbt925.weatherapp.design.theme.SizeM
import com.mbt925.weatherapp.design.theme.SizeS
import com.mbt925.weatherapp.design.theme.SizeXXL
import com.mbt925.weatherapp.feature.ui.utils.TestTags

@Composable
internal fun WeatherScreenLayout(
    isVertical: Boolean,
    toolbar: @Composable (isHorizontal: Boolean) -> Unit,
    nowWeather: @Composable (modifier: Modifier) -> Unit,
    todayWeather: @Composable (modifier: Modifier, horizontalPadding: Dp) -> Unit,
    snackBar: @Composable (modifier: Modifier, contentModifier: Modifier) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
    ) { contentPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (isVertical) {
                Column(
                    modifier = Modifier
                        .testTag(TestTags.VerticalScreenLayout)
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding)
                        .systemBarsPadding()
                        .padding(SizeM)
                        .padding(bottom = SizeXXL)
                ) {
                    toolbar(isHorizontal = true)
                    nowWeather(Modifier.fillMaxWidth())
                    todayWeather(Modifier.fillMaxWidth(), 0.dp)
                }
            } else {
                Row(
                    modifier = Modifier
                        .testTag(TestTags.HorizontalScreenLayout)
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding)
                        .systemBarsPadding()
                        .padding(SizeM)
                        .padding(bottom = SizeXXL)
                ) {
                    toolbar(isHorizontal = false)
                    nowWeather(Modifier.weight(1f))
                    todayWeather(Modifier.weight(1f), SizeS)
                }
            }
            snackBar(Modifier.align(Alignment.BottomCenter), Modifier.padding(SizeM))
        }
    }
}
