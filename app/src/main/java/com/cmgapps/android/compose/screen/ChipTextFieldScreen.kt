/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.cmgapps.android.compose.ui.composable.ChipTextField

private val options = List(10) { "Foo$it" }

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipTextFieldScreen(
    backButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Chip Text Field") }, navigationIcon = backButton)
        },
    ) { contentPadding ->
        Column(
            modifier =
                Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
        ) {
            val (text, setText) = remember { mutableStateOf("") }

            val items = remember { mutableStateListOf<String>() }

            ChipTextField(
                modifier = Modifier.padding(contentPadding),
                text = text,
                onTextChange = setText,
                chips = items,
                onAddChip = items::add,
                onRemoveChip = items::remove,
                suggestions =
                    if (text.isBlank()) {
                        emptyList()
                    } else {
                        options.filteredBy(text)
                    },
            )
        }
    }
}

/**
 * Returns the element of [this] list that contain [text] as a subsequence,
 * with the subsequence underlined as an [AnnotatedString].
 */
fun List<String>.filteredBy(text: String): List<AnnotatedString> {
    fun underlineSubsequence(
        needle: String,
        haystack: String,
    ): AnnotatedString? {
        return buildAnnotatedString {
            var i = 0
            for (char in needle) {
                val start = i
                haystack.indexOf(char, startIndex = i, ignoreCase = true).let {
                    if (it < 0) {
                        return null
                    } else {
                        i = it
                    }
                }
                append(haystack.substring(start, i))
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(haystack[i])
                }
                i += 1
            }
            append(haystack.substring(i, haystack.length))
        }
    }
    return this.mapNotNull { option -> underlineSubsequence(text, option) }
}
