/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cmgapps.android.compose.toLocalTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

class TimePickerViewModel(
    clock: Clock,
) : ViewModel() {
    var selectedTime by mutableStateOf(clock.now().toLocalTime())
        private set

    fun updateTime(time: LocalTime) {
        selectedTime = time
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val savedStateHandle = createSavedStateHandle()
                    TimePickerViewModel(
                        clock = Clock.System,
                    )
                }
            }
    }
}
