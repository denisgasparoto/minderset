package com.denisgasparoto.minderset.domain.model

internal data class FlashCard(
    val id: Int = 0,
    val question: String,
    val answer: String,
    val category: String
)