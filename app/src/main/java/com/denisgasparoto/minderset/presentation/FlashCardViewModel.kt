package com.denisgasparoto.minderset.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.model.ValidationError
import com.denisgasparoto.minderset.domain.usecase.AddFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.DeleteFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.GetFlashCardsUseCase
import com.denisgasparoto.minderset.domain.usecase.ValidateFlashCardUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FlashCardViewModel(
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
    private val addFlashCardUseCase: AddFlashCardUseCase,
    private val deleteFlashCardUseCase: DeleteFlashCardUseCase,
    private val validateFlashCardUseCase: ValidateFlashCardUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashCardUiState())
    val uiState: StateFlow<FlashCardUiState> = _uiState.asStateFlow()

    private val _filterCategory = MutableStateFlow<String?>(null)
    val filterCategory: StateFlow<String?> = _filterCategory.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching { getFlashCardsUseCase() }
                .onSuccess { cards ->
                    val distinctCategories = cards.map { it.category }
                        .filter { it.isNotBlank() }
                        .distinct()
                        .sorted()

                    val filtered = filterCards(cards, _filterCategory.value)

                    _uiState.update {
                        it.copy(
                            cards = cards,
                            categories = distinctCategories,
                            filteredCards = filtered,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun setFilterCategory(category: String?) {
        _filterCategory.value = category
        _uiState.update {
            it.copy(
                selectedCategory = category,
                filteredCards = filterCards(it.cards, category)
            )
        }
    }

    private fun filterCards(allCards: List<FlashCard>, category: String?): List<FlashCard> {
        val filtered = category?.takeIf { it.isNotBlank() }?.let {
            allCards.filter { card -> card.category.equals(it, ignoreCase = true) }
        } ?: allCards

        return filtered.sortedWith(
            compareBy(
                { it.category.lowercase() },
                { it.question.lowercase() })
        )
    }

    fun addCard(question: String, answer: String, category: String) {
        viewModelScope.launch(ioDispatcher) {
            val card = FlashCard(question = question, answer = answer, category = category.trim())

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                addFlashCardUseCase(card)
            }.onSuccess {
                loadCards()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.message, isLoading = false)
                }
            }
        }
    }

    fun deleteCard(card: FlashCard) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching { deleteFlashCardUseCase(card) }
                .onSuccess {
                    loadCards()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun validateCard(question: String, answer: String): Map<String, ValidationError> {
        return validateFlashCardUseCase(question, answer)
    }
}