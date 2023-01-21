package com.mbt925.weatherapp.feature.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.mbt925.weatherapp.design.theme.WeatherAppTheme
import com.mbt925.weatherapp.feature.ui.models.WeatherParam
import com.mbt925.weatherapp.feature.ui.models.WeatherScreenParam
import com.mbt925.weatherapp.feature.ui.models.WeatherTypeParam
import com.mbt925.weatherapp.feature.ui.utils.TestTags
import java.time.OffsetDateTime
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun isLoadingShown() {
        initScreen()

        composeTestRule
            .onNodeWithTag(TestTags.LoadingWeatherData)
            .assertIsDisplayed()
    }

    @Test
    fun onCompactScreen_isVerticalLayoutUsed_isHorizontalToolbarShown() {
        initScreen()

        composeTestRule
            .onNodeWithTag(TestTags.VerticalScreenLayout)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.HorizontalToolbar)
            .assertIsDisplayed()
    }

    @Test
    fun onExtendedScreen_isHorizontalLayoutUsed_isVerticalToolbarShown() {
        initScreen(isVertical = false)

        composeTestRule
            .onNodeWithTag(TestTags.HorizontalScreenLayout)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.VerticalToolbar)
            .assertIsDisplayed()
    }

    @Test
    fun onOnlyNowWeather_isNowWeatherShown_isTodayWeatherHidden() {
        initScreen(onlyNowWeatherParam)

        composeTestRule
            .onNodeWithTag(TestTags.NowWeatherBox)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.TodayWeatherBox)
            .assertDoesNotExist()
    }

    @Test
    fun onFullWeather_isNowWeatherShown_isTodayWeatherShown() {
        initScreen(fullWeatherParam)

        composeTestRule
            .onNodeWithTag(TestTags.NowWeatherBox)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.TodayWeatherBox)
            .assertIsDisplayed()
    }

    private val weather = WeatherParam(
        dateTime = OffsetDateTime.MIN,
        temperature = 1f,
        pressure = 1,
        windSpeed = 2,
        humidity = 3,
        type = WeatherTypeParam.Foggy,
    )
    private val onlyNowWeatherParam = WeatherScreenParam(
        isLoading = false,
        nowWeather = weather,
    )
    private val fullWeatherParam = WeatherScreenParam(
        isLoading = false,
        nowWeather = weather,
        todayWeather = listOf(weather),
    )

    private fun initScreen(
        param: WeatherScreenParam = WeatherScreenParam(),
        isVertical: Boolean = true,
    ) {
        composeTestRule.setContent {
            WeatherAppTheme {
                WeatherScreen(
                    param = param.copy(hasVerticalLayout = isVertical),
                    onRetry = {},
                    onAskForPermission = {},
                    onToggleTemperatureUnit = {},
                    onToggleWindSpeedUnit = {},
                )
            }
        }
    }
}
