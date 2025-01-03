/*
 * Copyright (c) 2025. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.R

private const val MIN_DIGITS = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldTransformationScreen(
    modifier: Modifier = Modifier,
    backButton: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.testTag("TextFieldTransformationScreen"),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.textfield_transformation)) },
                navigationIcon = backButton,
            )
        },
    ) { contentPadding ->

        var text by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(contentPadding),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter { char -> char.isDigit() } },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .semantics {
                            contentDescription = "Input Field with visual transformation"
                        },
                visualTransformation = DashTransformation(4),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = text.length < MIN_DIGITS,
                supportingText = {
                    if (text.length < MIN_DIGITS) {
                        Text(
                            stringResource(
                                R.string.invalid_input,
                                MIN_DIGITS,
                            ),
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text)
        }
    }
}

class DashTransformation(
    private val digits: Int = 4,
) : VisualTransformation {
    private val creditCardOffsetTranslator =
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset + (offset / digits)

            override fun transformedToOriginal(offset: Int): Int = offset - (offset / digits)
        }

    override fun filter(text: AnnotatedString): TransformedText {
        val plainText = text.text
        val out =
            buildString {
                for (i in plainText.indices) {
                    append(plainText[i])
                    if (i % digits == digits - 1) {
                        append('-')
                    }
                }
            }

        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }
}
