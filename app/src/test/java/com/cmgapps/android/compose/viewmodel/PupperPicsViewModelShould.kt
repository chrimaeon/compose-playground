package com.cmgapps.android.compose.viewmodel

import app.cash.molecule.RecompositionMode
import app.cash.paparazzi.Paparazzi
import app.cash.turbine.test
import com.cmgapps.android.compose.screen.molecule.Breed
import com.cmgapps.android.compose.screen.molecule.Model
import com.cmgapps.android.compose.service.PupperPicsService
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PupperPicsViewModelShould {
    private lateinit var service: FakePupperPicsService

    @get:Rule
    val paparazzi = Paparazzi()

    @Before
    fun setUp() {
        service = FakePupperPicsService()
    }

    @Test
    fun `on launch, breeds are loaded and then image url`() =
        runTest {
            val testDispatcher = StandardTestDispatcher(testScheduler)

            PupperPicsViewModel(
                service,
                mainContext = testDispatcher,
                mode = RecompositionMode.Immediate,
            ).models.test {
                assertThat(
                    awaitItem(),
                    `is`(Model(loading = true, breeds = emptyList(), dropdownText = null, currentUrl = null)),
                )

                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = service.breeds,
                            dropdownText = service.breeds.first().name,
                            currentUrl = "foo",
                        ),
                    ),
                )
            }
        }
}

private class FakePupperPicsService : PupperPicsService {
    val breeds = listOf(Breed("Breed 1", "breed1"), Breed("Breed 2", "breed2"))

    override suspend fun listBreeds(): List<Breed> = breeds

    override suspend fun randomImageUrlFor(breed: String): String = "foo"
}
