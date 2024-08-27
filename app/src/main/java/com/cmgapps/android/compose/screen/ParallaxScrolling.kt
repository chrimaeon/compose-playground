/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmgapps.android.compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxScrollingScreen(
    modifier: Modifier = Modifier,
    backButton: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.parallax_scrolling))
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    ),
                navigationIcon = backButton,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->

        val direction = LocalLayoutDirection.current

        val lazyListState = rememberLazyListState()

        val parallax by remember {
            derivedStateOf {
                val layoutInfo = lazyListState.layoutInfo
                val itemInfo =
                    layoutInfo.visibleItemsInfo.firstOrNull {
                        it.key == "header"
                    } ?: return@derivedStateOf 0f

                val adjustedOffset = itemInfo.offset - layoutInfo.viewportStartOffset
                (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1f, 1f)
            }
        }

        LazyColumn(
            modifier =
                Modifier
                    .padding(
                        start = contentPadding.calculateStartPadding(direction),
                        end = contentPadding.calculateEndPadding(direction),
                        bottom = contentPadding.calculateBottomPadding(),
                    ).fillMaxHeight(),
            state = lazyListState,
        ) {
            item(key = "header", contentType = ContentType.Header) {
                Box(
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .aspectRatio(16f / 9)
                            .fillMaxWidth()
                            .animateItem(),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data("file:///android_asset/cupcake1.jpg")
                                .crossfade(true)
                                .placeholder(R.drawable.cupcake_placeholder)
                                .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = BiasAlignment(verticalBias = parallax * 0.5f, horizontalBias = 0f),
                    )
                    Text(
                        stringResource(R.string.cupcake),
                        modifier =
                            Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                                    shape = RoundedCornerShape(topEnd = 8.dp),
                                ).padding(8.dp),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            itemsIndexed(
                List(100) { it },
                key = { _, item -> "item$item" },
                contentType = { _, _ -> ContentType.Item },
            ) { index, _ ->
                Log.d("ListItem", "Index: $index")
                ListItem(headlineContent = { Text("Item $index") })
            }
        }
    }
}

private enum class ContentType {
    Header,
    Item,
}
