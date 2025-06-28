package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.repository.FlashCardRepository
import com.denisgasparoto.minderset.domain.model.FlashCard

internal class AddFlashCardUseCase(private val repository: FlashCardRepository) {
    suspend operator fun invoke(card: FlashCard) = repository.addFlashCard(card)
}