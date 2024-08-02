/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.cmgapps.android.compose.R

val bodyFontFamily =
    FontFamily(
        Font(
            R.font.outfit_regular,
            FontWeight.Normal,
        ),
        Font(
            R.font.outfit_thin,
            FontWeight.Thin,
        ),
        Font(
            R.font.outfit_semibold,
            FontWeight.SemiBold,
        ),
        Font(
            R.font.outfit_medium,
            FontWeight.Medium,
        ),
        Font(
            R.font.outfit_light,
            FontWeight.Light,
        ),
        Font(
            R.font.outfit_extralight,
            FontWeight.ExtraLight,
        ),
        Font(
            R.font.outfit_extrabold,
            FontWeight.ExtraBold,
        ),
        Font(
            R.font.outfit_bold,
            FontWeight.Bold,
        ),
        Font(
            R.font.outfit_black,
            FontWeight.Black,
        ),
    )

val displayFontFamily =
    FontFamily(
        Font(
            R.font.poppins_regular,
            FontWeight.Normal,
        ),
        Font(
            R.font.poppins_thinitalic,
            FontWeight.Thin,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_thin,
            FontWeight.Thin,
        ),
        Font(
            R.font.poppins_semibolditalic,
            FontWeight.SemiBold,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_semibold,
            FontWeight.SemiBold,
        ),
        Font(
            R.font.poppins_mediumitalic,
            FontWeight.Medium,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_medium,
            FontWeight.Medium,
        ),
        Font(
            R.font.poppins_lightitalic,
            FontWeight.Light,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_light,
            FontWeight.Light,
        ),
        Font(
            R.font.poppins_italic,
            FontWeight.Normal,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_extralightitalic,
            FontWeight.ExtraLight,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_extralight,
            FontWeight.ExtraLight,
        ),
        Font(
            R.font.poppins_extrabolditalic,
            FontWeight.ExtraBold,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_extrabold,
            FontWeight.ExtraBold,
        ),
        Font(
            R.font.poppins_bolditalic,
            FontWeight.Bold,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_bold,
            FontWeight.Bold,
        ),
        Font(
            R.font.poppins_blackitalic,
            FontWeight.Black,
            FontStyle.Italic,
        ),
        Font(
            R.font.poppins_black,
            FontWeight.Black,
        ),
    )

val baseline = Typography()

val AppTypography =
    Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
