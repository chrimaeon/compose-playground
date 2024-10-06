/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.SdkSuppress
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test

class ThemeShould {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    fun handleDynamicLightColorTheme() {
        composeTestRule
            .setContent {
                Theme(
                    dynamicColor = true,
                    darkTheme = false,
                ) {
                    val color = dynamicLightColorScheme(composeTestRule.activity).primary
                    assertThat(MaterialTheme.colorScheme.primary, `is`(color))
                }
            }
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    fun handleDynamicDarkColorTheme() {
        composeTestRule
            .setContent {
                Theme(
                    dynamicColor = true,
                    darkTheme = true,
                ) {
                    val color = dynamicDarkColorScheme(composeTestRule.activity).primary
                    assertThat(MaterialTheme.colorScheme.primary, `is`(color))
                }
            }
    }

    @Test
    fun handleLightColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = false,
                contrast = Contrast.Normal,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(lightScheme.primary))
            }
        }
    }

    @Test
    fun handleDarkColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = true,
                contrast = Contrast.Normal,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(darkScheme.primary))
            }
        }
    }

    @Test
    fun handleMediumContrastLightColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = false,
                contrast = Contrast.Medium,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(mediumContrastLightColorScheme.primary))
            }
        }
    }

    @Test
    fun handleMediumContrastDarkColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = true,
                contrast = Contrast.Medium,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(mediumContrastDarkColorScheme.primary))
            }
        }
    }

    @Test
    fun handleHighContrastLightColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = false,
                contrast = Contrast.High,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(highContrastLightColorScheme.primary))
            }
        }
    }

    @Test
    fun handleHighContrastDarkColorTheme() {
        composeTestRule.setContent {
            Theme(
                dynamicColor = false,
                darkTheme = true,
                contrast = Contrast.High,
            ) {
                assertThat(MaterialTheme.colorScheme.primary, `is`(highContrastDarkColorScheme.primary))
            }
        }
    }
}
