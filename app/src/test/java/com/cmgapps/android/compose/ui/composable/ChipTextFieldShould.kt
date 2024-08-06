package com.cmgapps.android.compose.ui.composable

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class ChipTextFieldShould : PaparazziTest() {
    @Test
    fun `render chips`() {
        paparazzi.snapshot {
            ChipTextField(
                text = "",
                chips = listOf("Bar", "Foo"),
                onTextChange = {},
                onAddChip = {},
                onRemoveChip = {},
                suggestions = emptyList(),
            )
        }
    }
}
