package com.cmgapps.android.compose.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Ignore
import org.junit.Test

class SharedElementTransitionScreenShould : PaparazziTest() {
    @Ignore("Software rendering doesn't support drawRenderNode")
    @Test
    fun `render list`() {
        paparazzi
            .snapshot {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides
                        object : ViewModelStoreOwner {
                            override val viewModelStore = ViewModelStore()
                        },
                ) {
                    SharedElementTransitionScreen()
                }
            }
    }
}
