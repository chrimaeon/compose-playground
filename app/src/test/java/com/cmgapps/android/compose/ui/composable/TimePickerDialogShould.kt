package com.cmgapps.android.compose.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import com.cmgapps.android.compose.test.PaparazziTest
import kotlinx.datetime.LocalTime
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class TimePickerDialogShould : PaparazziTest() {
    @Test
    fun `render time picker dialog`() {
        paparazzi.snapshot {
            TimeDailer(
                initialTime = LocalTime(11, 29),
                onConfirm = { },
                onDismiss = { },
            )
        }
    }

    @Test
    fun `render time picker dialog with invalid time`() {
        paparazzi.snapshot {
            TimeDailer(
                initialTime = LocalTime(13, 45),
                onConfirm = { },
                onDismiss = { },
            )
        }
    }
}
