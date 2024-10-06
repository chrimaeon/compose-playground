/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose

import android.app.Application
import android.webkit.WebView
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.google.android.material.color.ColorContrast
import com.google.android.material.color.ColorContrastOptions
import com.google.android.material.color.DynamicColors
import timber.log.Timber

class Application :
    Application(),
    ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader =
        ImageLoader
            .Builder(this)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }.build()

    override fun onCreate() {
        super.onCreate()
        when {
            DynamicColors.isDynamicColorAvailable() ->
                DynamicColors.applyToActivitiesIfAvailable(this)

            ColorContrast.isContrastAvailable() ->
                ColorContrast.applyToActivitiesIfAvailable(
                    this,
                    ColorContrastOptions
                        .Builder()
                        .setMediumContrastThemeOverlay(R.style.ThemeOverlay_AppTheme_MediumContrast)
                        .setHighContrastThemeOverlay(R.style.ThemeOverlay_AppTheme_HighContrast)
                        .build(),
                )
        }

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
