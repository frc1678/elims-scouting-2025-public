package com.frc1678.driver_practice.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class Styles {
    companion object {

        // Card Colors
        val unselectedRed = CardColors(
            Color(255, 102, 89),
            Color(255, 102, 89),
            Color(255, 102, 89),
            Color(255, 102, 89)
        )

        val selectedRed = CardColors(
            Color(208, 55, 44),
            Color(208, 55, 44),
            Color(208, 55, 44),
            Color(208, 55, 44)
        )

        val unselectedBlue = CardColors(
            Color(51, 153, 255),
            Color(51, 153, 255),
            Color(51, 153, 255),
            Color(51, 153, 255)
        )

        val selectedBlue = CardColors(
            Color(20, 112, 205),
            Color(20, 112, 205),
            Color(20, 112, 205),
            Color(20, 112, 205)
        )

        val defaultLightModeButton = CardColors(
            Color.LightGray,
            Color.Black,
            Color.LightGray,
            Color.DarkGray,
        )

        val defaultDarkModeButton = CardColors(
            Color.Gray,
            Color.White,
            Color.Gray,
            Color.LightGray,
        )

        // Border Styles
        val unselectedRedBorder = BorderStroke(10.dp, Color(208, 55, 44))
        val unselectedBlueBorder = BorderStroke(10.dp, Color(20, 112, 205))
        val darkSelectedBorder = BorderStroke(10.dp, Color(0, 0, 0))
        val lightSelectedBorder = BorderStroke(10.dp, Color(255, 255, 255))
    }
}
