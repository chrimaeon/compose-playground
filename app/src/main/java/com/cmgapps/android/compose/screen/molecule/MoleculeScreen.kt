/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen.molecule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.viewmodel.PupperPicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoleculeScreen(
    modifier: Modifier = Modifier,
    viewModel: PupperPicsViewModel = viewModel(factory = PupperPicsViewModel.Factory),
    backButton: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier.testTag("MoleculeScreen"),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.molecule)) },
                navigationIcon = backButton,
            )
        },
    ) { innerPadding ->

        val model by viewModel.models.collectAsStateWithLifecycle()
        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(!model.loading) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = it
                    },
                ) {
                    TextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = model.dropdownText ?: stringResource(R.string.select_breed),
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        label = { Text(stringResource(R.string.dog_breed)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        model.breeds.forEach { breed ->
                            DropdownMenuItem(
                                text = { Text(breed.name, style = MaterialTheme.typography.bodyLarge) },
                                onClick = {
                                    viewModel.take(Event.SelectBreed(breed))
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Content(
                modifier = Modifier.weight(1f),
                model = model,
                onClick = { viewModel.take(Event.FetchAgain) },
            )
        }
    }
}

@Composable
private fun Content(
    model: Model,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var imageLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(
                        onClickLabel =
                            stringResource(
                                R.string.load_another_image,
                                model.dropdownText ?: "",
                            ),
                        onClick = onClick,
                    ),
            model = model.currentUrl,
            onState = { imageLoading = it !is AsyncImagePainter.State.Success },
            contentDescription = stringResource(R.string.content_description_dog_image, model.dropdownText ?: ""),
        )
        AnimatedVisibility(
            model.loading || imageLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Loading(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    val rotation by rememberInfiniteTransition("loading animation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1_000)),
        label = "loading rotation",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "🐶",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.rotate(rotation),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
