/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.toLocalTime
import com.cmgapps.android.compose.ui.composable.TimeDailer
import com.cmgapps.android.compose.viewmodel.TimePickerViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerScreen(
    backButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    initialTime: LocalTime =
        Clock.System
            .now()
            .toLocalTime(),
    viewModel: TimePickerViewModel = viewModel(factory = TimePickerViewModel.Factory),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.time_picker)) }, navigationIcon = backButton)
        },
    ) { contentPadding ->

        LaunchedEffect(initialTime) {
            viewModel.updateTime(initialTime)
        }

        val selectedTime = viewModel.selectedTime
        var pickerVisible by remember { mutableStateOf(false) }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 48.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .clickable { pickerVisible = true },
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { pickerVisible = true },
                contentPadding = ButtonDefaults.TextButtonWithIconContentPadding,
            ) {
                Text(
                    selectedTime.format(
                        LocalTime.Format {
                            hour()
                            char(':')
                            minute()
                        },
                    ),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.EditCalendar, contentDescription = stringResource(R.string.pick_a_time))
            }
        }
        if (pickerVisible) {
            TimeDailer(
                initialTime = selectedTime,
                onConfirm = {
                    val time = LocalTime(it.hour, it.minute)
                    viewModel.updateTime(time)
                    pickerVisible = false
                },
                onDismiss = {
                    pickerVisible = false
                },
            )
        }
    }
}
