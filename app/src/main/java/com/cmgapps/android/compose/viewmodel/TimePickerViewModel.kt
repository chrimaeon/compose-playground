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
import com.cmgapps.android.compose.toLocalTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

class TimePickerViewModel : ViewModel() {
    var selectedTime by mutableStateOf(Clock.System.now().toLocalTime())
        private set

    fun updateTime(time: LocalTime) {
        selectedTime = time
    }
}
