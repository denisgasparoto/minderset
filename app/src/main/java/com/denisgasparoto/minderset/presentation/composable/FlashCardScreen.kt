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

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.flash_cards_title)) }) },
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
                FlashCardList(
                    cards = uiState.cards,
                    onDelete = { card -> viewModel.deleteCard(card) },
                    onShare = { card -> card.share(context) },
                    onCardClick = { card -> selectedCardForDetails = card },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }

    AddFlashCardDialog(
        isOpen = showDialog,
        onDismiss = { showDialog = false },
        onSave = { question, answer ->
            val newCard = FlashCard(
                question = question,
                answer = answer
            )
            viewModel.addCard(newCard)
            showDialog = false
        },
        validate = viewModel::validateCard
    )

    selectedCardForDetails?.let { card ->
        FlashCardDetailsDialog(
            card = card,
            onDismiss = { selectedCardForDetails = null }
        )
    }
}