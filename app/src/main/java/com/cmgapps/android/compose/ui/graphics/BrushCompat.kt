/*
 * Copyright (c) 2026. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.ui.graphics

import android.os.Build
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.romainguy.vibrance.compose.verticalPigmentsGradient

object BrushCompat {
    @JvmStatic
    fun verticalGradient(
        startColor: Color,
        endColor: Color,
    ): Brush =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Brush.verticalPigmentsGradient(startColor = startColor, endColor = endColor)
        } else {
            Brush.verticalGradient(
                listOf(
                    startColor,
                    endColor,
                ),
            )
        }
}
