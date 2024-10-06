/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.cmgapps.android.compose.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimateItemScreen(
    modifier: Modifier = Modifier,
    backButton: @Composable () -> Unit = {},
    random: Random = Random.Default,
) {
    val items = remember { mutableStateListOf("Item 1") }

    Scaffold(
        modifier = modifier.testTag("AnimateItemScreen"),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.animate_item)) },
                navigationIcon = backButton,
                actions = {
                    IconButton(
                        onClick = {
                            val index = random.nextInt(items.size)
                            items.add(index, "Item ${items.size + 1}")
                        },
                    ) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(R.string.content_description_add_item_to_list),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
        ) {
            items(items = items) { item ->
                ListItem(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .animateItem(),
                    headlineContent = { Text(item) },
                    trailingContent = {
                        IconButton(
                            onClick = { items.remove(item) },
                            enabled = items.size > 1,
                        ) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                "Delete $item",
                            )
                        }
                    },
                )
            }
        }
    }
}
