package com.vijanthi.computervathiyar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vijanthi.computervathiyar.R

val ttNorms = FontFamily(
        Font(R.font.tt_norms_pro_medium, weight = FontWeight.Medium),
        Font(R.font.tt_norms_pro_bold, weight = FontWeight.Bold),
        Font(R.font.tt_norms_pro_light, weight = FontWeight.Light),
        Font(R.font.tt_norms_pro_thin, weight = FontWeight.Thin),
        Font(R.font.tt_norms_pro_regular, weight = FontWeight.Normal),
        Font(R.font.tt_norms_pro_extrabold, weight = FontWeight.ExtraBold)
)
// Set of Material typography styles to start with
val Typography = Typography(
        bodyMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = PrimaryTextColor
        ),
        displayLarge = TextStyle(
                fontSize = 24.sp,
                fontFamily = ttNorms,
                fontWeight = FontWeight.Bold
        ),
        displayMedium = TextStyle(
                fontSize = 18.sp,
                fontFamily = ttNorms,
                fontWeight = FontWeight.Medium
        ),
        displaySmall = TextStyle(
                fontSize = 16.sp,
                fontFamily = ttNorms,
                fontWeight = FontWeight.Normal
        ),
        /* Other default text styles to override
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        ),
        caption = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
        */
)