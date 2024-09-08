/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import com.cmgapps.LogTag
import com.cmgapps.android.compose.screen.Dashboard
import com.cmgapps.android.compose.ui.theme.Theme
import timber.log.Timber

@LogTag
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val data = intent.data
        Timber.tag(LOG_TAG).d("onCreate: data=%s", data)

        setContent {
            Theme {
                Dashboard(deepLink = data)
            }
        }
    }
}
