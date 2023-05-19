package com.nanotricks.techysaw.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

class ScreenSize {
    @Composable
    fun height(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenHeightDp
    }
    @Composable
    fun width(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }

    @Composable
    fun widthPercent(percentage: Int): Int {
        return width() * (percentage/100)
    }

    @Composable
    fun heightPercent(percentage: Int): Int {
        return height() * (percentage/100)
    }
    @Composable
    fun heightOf(by: Int): Int {
        return height()/by
    }
}
