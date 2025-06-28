package com.denisgasparoto.minderset.domain.model

sealed class ValidationError {
    object QuestionTooShort : ValidationError()
    object QuestionTooLong : ValidationError()
    object AnswerTooShort : ValidationError()
    object AnswerTooLong : ValidationError()
}
