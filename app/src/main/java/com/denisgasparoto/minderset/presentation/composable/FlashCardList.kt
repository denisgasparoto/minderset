package com.denisgasparoto.minderset.presentation.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.presentation.theme.Dimens

@Composable
internal fun FlashCardList(
    cards: List<FlashCard>,
    onDelete: (FlashCard) -> Unit,
    onShare: (FlashCard) -> Unit,
    onCardClick: (FlashCard) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.SpaceLarge)
    ) {
        items(cards) { card ->
            FlashCardItem(
                card = card,
                onDelete = onDelete,
                onShare = onShare,
                onClick = { onCardClick(card) }
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        }
    }
}
