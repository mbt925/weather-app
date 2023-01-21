package com.mbt925.weatherapp.design.platform

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion as MaterialWindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion as MaterialWindowWidthSizeClass
import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
class WindowSizeClass constructor(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass,
) {
    companion object {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        @Composable
        fun calculateFrom(activity: Activity) = calculateWindowSizeClass(activity).run {
            val width = when (widthSizeClass) {
                MaterialWindowWidthSizeClass.Compact -> WindowWidthSizeClass.Compact
                MaterialWindowWidthSizeClass.Medium -> WindowWidthSizeClass.Medium
                else -> WindowWidthSizeClass.Extended
            }
            val height = when (heightSizeClass) {
                MaterialWindowHeightSizeClass.Compact -> WindowHeightSizeClass.Compact
                MaterialWindowHeightSizeClass.Medium -> WindowHeightSizeClass.Medium
                else -> WindowHeightSizeClass.Extended
            }
            WindowSizeClass(
                widthSizeClass = width,
                heightSizeClass = height,
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as WindowSizeClass

        if (widthSizeClass != other.widthSizeClass) return false
        if (heightSizeClass != other.heightSizeClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = widthSizeClass.hashCode()
        result = 31 * result + heightSizeClass.hashCode()
        return result
    }

    override fun toString() = "WindowSizeClass($widthSizeClass, $heightSizeClass)"
}

enum class WindowWidthSizeClass {
    Compact, Medium, Extended
}

enum class WindowHeightSizeClass {
    Compact, Medium, Extended
}
