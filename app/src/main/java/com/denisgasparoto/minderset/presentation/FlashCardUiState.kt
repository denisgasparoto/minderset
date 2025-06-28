package com.denisgasparoto.minderset.presentation

import com.denisgasparoto.minderset.domain.model.FlashCard

internal data class FlashCardUiState(
    val cards: List<FlashCard> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)