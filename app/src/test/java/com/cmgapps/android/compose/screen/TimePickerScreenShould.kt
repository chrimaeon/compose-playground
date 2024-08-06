package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import com.cmgapps.android.compose.viewmodel.TimePickerViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.Test

class TimePickerScreenShould : PaparazziTest() {
    @Test
    fun `render time picker`() {
        paparazzi.snapshot {
            TimePickerScreen(
                backButton = {},
                initialTime = LocalTime(13, 12),
                viewModel =
                    TimePickerViewModel(
                        clock =
                            object :
                                Clock {
                                override fun now() = Instant.fromEpochMilliseconds(0)
                            },
                    ),
            )
        }
    }
}
