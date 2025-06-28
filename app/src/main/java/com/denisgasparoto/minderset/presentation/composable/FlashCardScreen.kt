package com.denisgasparoto.minderset.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.core.extensions.share
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.presentation.FlashCardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FlashCardScreen(viewModel: FlashCardViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var selectedCardForDetails by remember { mutableStateOf<FlashCard?>(null) }
    val selectedCategory by viewModel.filterCategory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.flash_cards_title)) },
                actions = {
                    CategoryFilterDropdown(
                        allCategories = uiState.cards.map { it.category }.distinct().sorted(),
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            viewModel.setFilterCategory(category)
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.fab_add_flashcard_desc)
                )
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.error_prefix, uiState.errorMessage ?: ""))
                }
            }

            else -> {
                val filteredCards = selectedCategory?.let { cat ->
                    uiState.cards.filter { it.category.equals(cat, ignoreCase = true) }
                } ?: uiState.cards

                FlashCardList(
                    cards = filteredCards,
                    onDelete = viewModel::deleteCard,
                    onShare = { it.share(context) },
                    onCardClick = { selectedCardForDetails = it },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }

    if (showDialog) {
        AddFlashCardDialog(
            isOpen = showDialog,
            onDismiss = { showDialog = false },
            onSave = { question, answer, category ->
                viewModel.addCard(
                    question = question,
                    answer = answer,
                    category = category
                )
                showDialog = false
            },
            validate = viewModel::validateCard,
            existingCategories = uiState.cards.map { it.category }.distinct().sorted()
        )
    }

    selectedCardForDetails?.let { card ->
        FlashCardDetailsDialog(
            card = card,
            onDismiss = { selectedCardForDetails = null }
        )
    }
}