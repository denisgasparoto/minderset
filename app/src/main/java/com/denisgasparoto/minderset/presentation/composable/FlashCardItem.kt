package com.denisgasparoto.minderset.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.core.extensions.truncate
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.presentation.theme.Dimens

@Composable
internal fun FlashCardItem(
    card: FlashCard,
    onDelete: (FlashCard) -> Unit,
    onShare: (FlashCard) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(Dimens.CardElevation)
    ) {
        Column(modifier = Modifier.padding(Dimens.SpaceLarge)) {
            Text(
                text = stringResource(R.string.prefix_question, card.question),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

            val truncatedAnswer = card.answer.truncate()

            Text(
                text = stringResource(R.string.prefix_answer, truncatedAnswer),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

            if (card.category.isNotBlank()) {
                Text(
                    text = stringResource(R.string.prefix_category, card.category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

            Row {
                Button(onClick = { onDelete(card) }) {
                    Text(stringResource(R.string.button_delete))
                }
                Spacer(modifier = Modifier.width(Dimens.SpaceSmall))
                Button(onClick = { onShare(card) }) {
                    Text(stringResource(R.string.button_share))
                }
            }
        }
    }
}