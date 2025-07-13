package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import com.cmgapps.android.compose.viewmodel.TimePickerViewModel
import kotlinx.datetime.LocalTime
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TimePickerScreenShould : PaparazziTest() {
    @Test
    fun `render time picker`() {
        @OptIn(ExperimentalTime::class)
        val viewModel =
            TimePickerViewModel(
                clock =
                    object :
                        Clock {
                        override fun now() = Instant.fromEpochMilliseconds(0)
                    },
            )
        paparazzi.snapshot {
            TimePickerScreen(
                backButton = {},
                initialTime = LocalTime(13, 12),
                viewModel = viewModel,
            )
        }
    }
}
