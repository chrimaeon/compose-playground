/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.screen.filteredBy
import com.cmgapps.android.compose.ui.theme.Theme

private val tagSeparator = "\\s".toRegex()

/**
 * ⚠️ Does not work correctly when hardware keyboard is used;
 *    looses focus after first chip is added
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ChipTextField(
    text: String,
    onTextChange: (String) -> Unit,
    chips: List<String>,
    onAddChip: (String) -> Unit,
    onRemoveChip: (String) -> Unit,
    suggestions: List<AnnotatedString>,
    modifier: Modifier = Modifier,
) {
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = suggestions.isNotEmpty() || allowExpanded

    LaunchedEffect(text) {
        snapshotFlow {
            text
        }.collect {
            tagSeparator.find(text)?.let {
                val tag = text.substring(0, it.range.last)
                if (tag.isNotBlank()) {
                    onAddChip(tag)
                }
                onTextChange("")
            }
        }
    }

    val focusRequester = remember { FocusRequester() }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = setExpanded,
    ) {
        val colors = TextFieldDefaults.colors()
        TextFieldDefaults.DecorationBox(
            colors = colors,
            value = "",
            innerTextField = {
                FlowRow(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                focusRequester.requestFocus()
                            }.menuAnchor(MenuAnchorType.PrimaryEditable),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    chips.forEach {
                        InputChip(
                            selected = false,
                            onClick = { onRemoveChip(it) },
                            label = { Text(it) },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = "remove")
                            },
                        )
                    }
                    Box(modifier = Modifier.padding(vertical = 14.dp)) {
                        BasicTextField(
                            modifier =
                                Modifier
                                    .width(IntrinsicSize.Min)
                                    .defaultMinSize(15.dp)
                                    .focusRequester(focusRequester),
                            maxLines = 1,
                            value = text,
                            onValueChange = onTextChange,
                            cursorBrush = SolidColor(colors.cursorColor),
                            textStyle = LocalTextStyle.current.copy(color = colors.focusedTextColor),
                        )
                    }
                }
            },
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = PaddingValues(horizontal = 16.dp),
        )

        ExposedDropdownMenu(
            modifier =
                Modifier
                    .fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
        ) {
            suggestions.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = {
                        onAddChip(it.text)
                        onTextChange("")
                        setExpanded(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
@PreviewScreenSizes
// @PreviewLightDark
// @PreviewDynamicColors
// @PreviewFontScale
private fun GreetingPreview() {
    Theme {
        ChipTextField(
            text = "Foo",
            onTextChange = {},
            chips = listOf("Bar", "foo", "cancel", "Bar", "foo", "cancel"),
            onAddChip = {},
            onRemoveChip = {},
            suggestions = List(10) { "Foo$it" }.filteredBy("Foo"),
        )
    }
}
