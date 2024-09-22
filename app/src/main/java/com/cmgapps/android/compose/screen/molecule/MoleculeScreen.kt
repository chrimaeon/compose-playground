/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.molecule)) },
                navigationIcon = backButton,
            )
        },
    ) { innerPadding ->

        val models by viewModel.models.collectAsStateWithLifecycle()
        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
        ) {
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
                    value = models.dropdownText ?: stringResource(R.string.select_breed),
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
                    models.breeds.forEach { breed ->
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

            Spacer(Modifier.height(16.dp))

            AsyncImage(
                modifier =
                    Modifier.fillMaxSize().clickable(
                        onClickLabel =
                            stringResource(
                                R.string.load_another_image,
                                models.dropdownText ?: "",
                            ),
                    ) {
                        viewModel.take(Event.FetchAgain)
                    },
                model = models.currentUrl,
                contentDescription = stringResource(R.string.content_description_dog_image, models.dropdownText ?: ""),
            )
        }
    }
}
