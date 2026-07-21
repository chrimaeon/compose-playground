package com.cmgapps.android.compose.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class TimePickerDialogShould : PaparazziTest() {
    @Test
    @Ignore("Paparazzi does not work with AGP9.x")
    fun `render time picker dialog`() {
        // paparazzi.snapshot {
        //     TimeDailer(
        //         initialTime = LocalTime(11, 29),
        //         onConfirm = { },
        //         onDismiss = { },
        //     )
        // }
    }

    @Test
    @Ignore("Paparazzi does not work with AGP9.x")
    fun `render time picker dialog with invalid time`() {
        // paparazzi.snapshot {
        //     TimeDailer(
        //         initialTime = LocalTime(13, 45),
        //         onConfirm = { },
        //         onDismiss = { },
        //     )
        // }
    }
}
