package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class TextFieldTransformationScreenShould : PaparazziTest() {
    @Test
    fun `render screen`() {
        paparazzi.snapshot {
            TextFieldTransformationScreen()
        }
    }
}
