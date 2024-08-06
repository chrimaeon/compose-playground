package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class ChipTextFieldScreenShould : PaparazziTest() {
    @Test
    fun `render chip text field screen`() {
        paparazzi.snapshot {
            ChipTextFieldScreen(backButton = { })
        }
    }
}
