package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class PullToRefreshScreenShould : PaparazziTest() {
    @Test
    fun `render pull to refresh screen`() {
        paparazzi.snapshot {
            PullToRefreshScreen(backButton = { })
        }
    }
}
