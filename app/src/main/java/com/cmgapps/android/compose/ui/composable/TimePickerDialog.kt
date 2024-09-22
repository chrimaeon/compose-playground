/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.cmgapps.LogTag
import com.cmgapps.android.compose.BuildConfig
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.toLocalTime
import com.cmgapps.android.compose.ui.theme.Theme
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LogTag
fun TimeDailer(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialTime: LocalTime =
        Clock.System
            .now()
            .toLocalTime(),
) {
    val timePickerState =
        rememberTimePickerState(
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
            is24Hour = true,
        )

    var hasError by remember {
        mutableStateOf(initialTime.hour > 12)
    }

    LaunchedEffect(timePickerState) {
        combine(
            snapshotFlow { timePickerState.hour },
            snapshotFlow { timePickerState.minute },
        ) { hour, minute -> hour to minute }
            .collect { (hour, minute) ->
                if (BuildConfig.DEBUG) {
                    Timber
                        .tag(ComposableTimeDailer.LOG_TAG)
                        .v("%02d:%02d", hour, minute)
                }
                hasError = hour > 12
            }
    }

    TimePickerDialog(
        modifier = modifier.semantics { testTag = "TimePickerDialog" },
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },
        onConfirmEnabled = !hasError,
    ) {
        TimePicker(
            state = timePickerState,
            colors =
                TimePickerDefaults.colors(
                    selectorColor =
                        if (hasError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                ),
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onConfirmEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }, enabled = onConfirmEnabled) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { content() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewFontScale
@PreviewLightDark
@Composable
private fun PickerWithErrorPreview() {
    val initialTime = LocalTime(15, 5)
    Theme {
        Scaffold { contentPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )

            TimeDailer(
                initialTime = initialTime,
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
private fun PickerPreview() {
    val initialTime = LocalTime(5, 12)

    Theme {
        Scaffold { contentPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )
            TimeDailer(
                initialTime = initialTime,
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}
