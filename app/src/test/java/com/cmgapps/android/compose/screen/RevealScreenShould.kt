package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class RevealScreenShould : PaparazziTest() {
    @Test
    fun `render reveal screen`() {
        paparazzi.snapshot {
            RevealScreen(backButton = { })
        }
    }
}
