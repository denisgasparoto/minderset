package com.denisgasparoto.minderset.presentation

import com.denisgasparoto.minderset.domain.model.FlashCard

internal data class FlashCardUiState(
    val cards: List<FlashCard> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList(),
    val filteredCards: List<FlashCard> = emptyList()
)