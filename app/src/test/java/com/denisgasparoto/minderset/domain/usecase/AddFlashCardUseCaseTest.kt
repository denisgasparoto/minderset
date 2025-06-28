package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.repository.FlashCardRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddFlashCardUseCaseTest {

    private lateinit var repository: FlashCardRepository
    private lateinit var useCase: AddFlashCardUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = AddFlashCardUseCase(repository)
    }

    @Test
    fun `invoke calls repository addFlashCard`() = runBlocking {
        val card = FlashCard(id = 1, question = "Q", answer = "A", category = "C")

        useCase(card)

        coVerify(exactly = 1) { repository.addFlashCard(card) }
    }
}

