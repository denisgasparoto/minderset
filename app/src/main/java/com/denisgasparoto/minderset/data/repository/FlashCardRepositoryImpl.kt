package com.denisgasparoto.minderset.data.repository

import com.denisgasparoto.minderset.data.datasource.FlashCardDao
import com.denisgasparoto.minderset.data.mapper.toDomain
import com.denisgasparoto.minderset.domain.mapper.toEntity
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.repository.FlashCardRepository

internal class FlashCardRepositoryImpl(private val dao: FlashCardDao) : FlashCardRepository {
    override suspend fun getFlashCards(): List<FlashCard> = dao.getAll().map { it.toDomain() }

    override suspend fun addFlashCard(card: FlashCard) {
        dao.insert(card.toEntity())
    }

    override suspend fun deleteFlashCard(card: FlashCard) {
        dao.delete(card.toEntity())
    }
}