package com.denisgasparoto.minderset.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.presentation.theme.Dimens

@Composable
internal fun FlashCardDetailsDialog(
    card: FlashCard,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close_button_text))
            }
        },
        title = { Text(stringResource(R.string.flash_card_details_title)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.label_question),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = card.question,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
                Text(
                    text = stringResource(R.string.label_answer),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = card.answer,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}
