package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.model.ValidationError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateFlashCardUseCaseTest {

    private lateinit var validateUseCase: ValidateFlashCardUseCase

    @Before
    fun setup() {
        validateUseCase = ValidateFlashCardUseCase()
    }

    @Test
    fun `valid inputs return empty error map`() {
        val question = "What is Kotlin?"
        val answer = "A programming language."

        val errors = validateUseCase(question, answer)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `question too short returns QuestionTooShort error`() {
        val question = "Hi"
        val answer = "Some answer"

        val errors = validateUseCase(question, answer)

        assertEquals(1, errors.size)
        assertEquals(ValidationError.QuestionTooShort, errors["question"])
    }

    @Test
    fun `question too long returns QuestionTooLong error`() {
        val question = "A".repeat(ValidationKeys.MAX_QUESTION_TEXT_LENGTH + 1)
        val answer = "Some answer"

        val errors = validateUseCase(question, answer)

        assertEquals(1, errors.size)
        assertEquals(ValidationError.QuestionTooLong, errors["question"])
    }

    @Test
    fun `answer too short returns AnswerTooShort error`() {
        val question = "Valid question?"
        val answer = "A"

        val errors = validateUseCase(question, answer)

        assertEquals(1, errors.size)
        assertEquals(ValidationError.AnswerTooShort, errors["answer"])
    }

    @Test
    fun `answer too long returns AnswerTooLong error`() {
        val question = "Valid question?"
        val answer = "A".repeat(ValidationKeys.MAX_ANSWER_TEXT_LENGTH + 1)

        val errors = validateUseCase(question, answer)

        assertEquals(1, errors.size)
        assertEquals(ValidationError.AnswerTooLong, errors["answer"])
    }

    @Test
    fun `both question and answer invalid return both errors`() {
        val question = "A"
        val answer = "A"

        val errors = validateUseCase(question, answer)

        assertEquals(2, errors.size)
        assertEquals(ValidationError.QuestionTooShort, errors["question"])
        assertEquals(ValidationError.AnswerTooShort, errors["answer"])
    }
}
