/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.cmgapps.android.compose.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshScreen(
    modifier: Modifier = Modifier,
    backButton: @Composable () -> Unit,
) {
    var itemCount by remember { mutableIntStateOf(15) }
    var isRefreshing by remember { mutableStateOf(false) }

    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit =
        remember {
            {
                isRefreshing = true
                coroutineScope.launch {
                    delay(1.seconds)
                    itemCount += 5
                    isRefreshing = false
                }
            }
        }

    Scaffold(
        modifier = modifier.semantics { testTag = "PullToRefreshScreen" },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.pull_2_refresh)) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, stringResource(R.string.trigger_refresh))
                    }
                },
                navigationIcon = backButton,
            )
        },
    ) {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(itemCount) { ListItem({ Text(text = "Item ${itemCount - it}") }) }
            }
        }
    }
}
