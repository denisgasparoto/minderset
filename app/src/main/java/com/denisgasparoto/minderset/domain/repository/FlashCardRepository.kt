package com.denisgasparoto.minderset.domain.repository

import com.denisgasparoto.minderset.domain.model.FlashCard

internal interface FlashCardRepository {
    suspend fun getFlashCards(): List<FlashCard>
    suspend fun addFlashCard(card: FlashCard)
    suspend fun deleteFlashCard(card: FlashCard)
}