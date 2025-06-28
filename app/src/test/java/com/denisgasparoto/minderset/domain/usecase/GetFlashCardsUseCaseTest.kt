package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.repository.FlashCardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetFlashCardsUseCaseTest {

    private lateinit var repository: FlashCardRepository
    private lateinit var useCase: GetFlashCardsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFlashCardsUseCase(repository)
    }

    @Test
    fun `invoke calls repository getFlashCards and returns data`() = runBlocking {
        val cards = listOf(
            FlashCard(id = 1, question = "Q1", answer = "A1", category = "C1"),
            FlashCard(id = 2, question = "Q2", answer = "A2", category = "C2")
        )
        coEvery { repository.getFlashCards() } returns cards

        val result = useCase()

        coVerify(exactly = 1) { repository.getFlashCards() }
        assertEquals(cards, result)
    }
}
