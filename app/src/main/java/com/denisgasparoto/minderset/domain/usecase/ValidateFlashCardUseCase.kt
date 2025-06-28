package com.denisgasparoto.minderset.domain.usecase

import com.denisgasparoto.minderset.domain.model.ValidationError

object ValidationKeys {
    const val QUESTION = "question"
    const val ANSWER = "answer"
    const val MAX_QUESTION_TEXT_LENGTH = 30
    const val MIN_QUESTION_TEXT_LENGTH = 3
    const val MAX_ANSWER_TEXT_LENGTH = 200
    const val MIN_ANSWER_TEXT_LENGTH = 3
}

internal class ValidateFlashCardUseCase {
    operator fun invoke(question: String, answer: String): Map<String, ValidationError> {
        val errors = mutableMapOf<String, ValidationError>()

        if (question.length < ValidationKeys.MIN_QUESTION_TEXT_LENGTH) {
            errors[ValidationKeys.QUESTION] = ValidationError.QuestionTooShort
        } else if (question.length > ValidationKeys.MAX_QUESTION_TEXT_LENGTH) {
            errors[ValidationKeys.QUESTION] = ValidationError.QuestionTooLong
        }

        if (answer.length < ValidationKeys.MIN_ANSWER_TEXT_LENGTH) {
            errors[ValidationKeys.ANSWER] = ValidationError.AnswerTooShort
        } else if (answer.length > ValidationKeys.MAX_ANSWER_TEXT_LENGTH) {
            errors[ValidationKeys.ANSWER] = ValidationError.AnswerTooLong
        }

        return errors
    }
}
