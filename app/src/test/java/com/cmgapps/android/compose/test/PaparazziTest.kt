/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.test

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule

abstract class PaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = PIXEL_5,
            theme = "Theme.Material3.Light.NoActionBar",
        )
}
