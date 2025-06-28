package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.repository.FlashCardRepository

internal class DeleteFlashCardUseCase(private val repository: FlashCardRepository) {
    suspend operator fun invoke(card: FlashCard) = repository.deleteFlashCard(card)
}
