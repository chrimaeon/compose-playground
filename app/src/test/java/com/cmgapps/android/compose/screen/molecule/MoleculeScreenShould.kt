package com.cmgapps.android.compose.screen.molecule

import com.cmgapps.android.compose.service.PupperPicsService
import com.cmgapps.android.compose.test.PaparazziTest
import com.cmgapps.android.compose.viewmodel.PupperPicsViewModel
import org.junit.Before
import org.junit.Test

class MoleculeScreenShould : PaparazziTest() {
    final lateinit var viewModel: PupperPicsViewModel

    @Before
    fun setUp() {
        viewModel = PupperPicsViewModel(FakePupperService())
    }

    @Test
    fun `render molecule screen`() {
        paparazzi.snapshot {
            MoleculeScreen(
                viewModel = viewModel,
                backButton = { },
            )
        }
    }
}

private class FakePupperService : PupperPicsService {
    override suspend fun listBreeds(): List<Breed> =
        listOf(
            Breed("Affenpinscher", "affenpinscher"),
            Breed("Pudel", "pudel"),
        )

    override suspend fun randomImageUrlFor(breed: String): String = ""
}
