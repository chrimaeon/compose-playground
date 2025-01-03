/*
 * Copyright (c) 2025. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class TextFieldTransformationScreenShould {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showTransformation() {
        composeTestRule.setContent {
            TextFieldTransformationScreen()
        }

        with(
            composeTestRule
                .onNodeWithContentDescription("Input Field with visual transformation"),
        ) {
            performTextInput("123456")
            assert(hasText("1234-56"))
        }
    }
}
