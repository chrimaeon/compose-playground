package com.cmgapps.android.compose.screen.molecule

import com.cmgapps.android.compose.service.PupperPicsService
import com.cmgapps.android.compose.test.PaparazziTest
import com.cmgapps.android.compose.viewmodel.PupperPicsViewModel
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class MoleculeScreenShould : PaparazziTest() {
    lateinit var viewModel: PupperPicsViewModel

    @Before
    fun setUp() {
        viewModel = PupperPicsViewModel(FakePupperService())
    }

    @Ignore("Paparazzi not supporting AGP 9.x")
    @Test
    fun `render molecule screen`() {
        // paparazzi.snapshot {
        //     MoleculeScreen(
        //         viewModel = viewModel,
        //         serverBaseUrl = "http://localhost:8080",
        //         backButton = { },
        //     )
        // }
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
