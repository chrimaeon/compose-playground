/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.ui.theme.onPrimaryContainerDark
import com.cmgapps.android.compose.ui.theme.onPrimaryContainerLight
import com.cmgapps.android.compose.ui.theme.onSecondaryContainerDark
import com.cmgapps.android.compose.ui.theme.onSecondaryContainerLight
import com.cmgapps.android.compose.ui.theme.primaryContainerDark
import com.cmgapps.android.compose.ui.theme.primaryContainerLight
import com.cmgapps.android.compose.ui.theme.secondaryContainerDark
import com.cmgapps.android.compose.ui.theme.secondaryContainerLight
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlin.random.Random

val items = List(100) { it }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun HazeScreen(
    modifier: Modifier = Modifier,
    random: Random = Random,
    backButton: @Composable () -> Unit,
) {
    val hazeState = remember { HazeState() }

    Scaffold(
        modifier = modifier.semantics { testTag = "HazeScreen" },
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.haze)) },
                colors = TopAppBarDefaults.largeTopAppBarColors(Color.Transparent),
                modifier =
                    Modifier
                        .hazeChild(
                            state = hazeState,
                            style = HazeMaterials.thin(),
                        ).fillMaxWidth(),
                navigationIcon = backButton,
            )
        },
    ) { contentPadding ->
        LazyVerticalGrid(
            modifier =
                Modifier
                    .haze(
                        state = hazeState,
                    ),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(top = contentPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items,
            ) {
                val (containerColor, contentColor) = remember { colors.random(random) }
                Card(
                    modifier = Modifier.height(100.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = containerColor,
                            contentColor = contentColor,
                        ),
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        Text("Item $it")
                    }
                }
            }
        }
    }
}

val colors =
    listOf(
        primaryContainerLight to onPrimaryContainerLight,
        primaryContainerDark to onPrimaryContainerDark,
        secondaryContainerLight to onSecondaryContainerLight,
        secondaryContainerDark to onSecondaryContainerDark,
    )
