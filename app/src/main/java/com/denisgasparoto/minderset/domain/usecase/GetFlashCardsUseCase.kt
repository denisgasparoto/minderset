package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.repository.FlashCardRepository

internal class GetFlashCardsUseCase(private val repository: FlashCardRepository) {
    suspend operator fun invoke() = repository.getFlashCards()
}