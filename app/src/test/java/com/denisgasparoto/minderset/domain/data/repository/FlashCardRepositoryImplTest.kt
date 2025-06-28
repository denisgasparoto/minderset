package com.denisgasparoto.minderset.domain.data.repository

import com.denisgasparoto.minderset.data.datasource.FlashCardDao
import com.denisgasparoto.minderset.data.model.FlashCardEntity
import com.denisgasparoto.minderset.data.repository.FlashCardRepositoryImpl
import com.denisgasparoto.minderset.domain.mapper.toEntity
import com.denisgasparoto.minderset.domain.model.FlashCard
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FlashCardRepositoryImplTest {

    private lateinit var dao: FlashCardDao
    private lateinit var repository: FlashCardRepositoryImpl

    @Before
    fun setup() {
        dao = mockk()
        repository = FlashCardRepositoryImpl(dao)
    }

    @Test
    fun `getFlashCards returns mapped list from dao`() = runBlocking {
        val entities = listOf(
            FlashCardEntity(id = 1, question = "Q1", answer = "A1"),
            FlashCardEntity(id = 2, question = "Q2", answer = "A2")
        )
        every { runBlocking { dao.getAll() } } returns entities

        val result = repository.getFlashCards()

        verify(exactly = 1) { runBlocking { dao.getAll() } }
        assertEquals(entities.size, result.size)
        assertEquals("Q1", result[0].question)
        assertEquals("A2", result[1].answer)
    }

    @Test
    fun `addFlashCard calls dao insert with entity`() = runBlocking {
        val card = FlashCard(id = 5, question = "Q", answer = "A")
        val entity = card.toEntity()

        coEvery { dao.insert(entity) } just Runs

        repository.addFlashCard(card)

        coVerify(exactly = 1) { dao.insert(entity) }
    }

    @Test
    fun `deleteFlashCard calls dao delete with entity`() = runBlocking {
        val card = FlashCard(id = 5, question = "Q", answer = "A")
        val entity = card.toEntity()

        coEvery { dao.delete(entity) } just Runs

        repository.deleteFlashCard(card)

        coVerify(exactly = 1) { dao.delete(entity) }
    }
}
