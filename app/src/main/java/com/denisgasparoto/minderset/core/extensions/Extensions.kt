package com.denisgasparoto.minderset.core.extensions

import android.content.Context
import android.content.Intent
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.domain.model.FlashCard

internal const val MAX_LENGTH = 50

internal fun FlashCard.toShareText(context: Context): String {
    return buildString {
        appendLine(context.getString(R.string.share_flashcard_title))
        appendLine(context.getString(R.string.share_flashcard_question, question))
        appendLine(context.getString(R.string.share_flashcard_answer, answer))
    }
}

internal fun FlashCard.share(context: Context) {
    val textToShare = this.toShareText(context)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, textToShare)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_chooser_title)
        )
    )
}

internal fun String.truncate(maxLength: Int = MAX_LENGTH): String {
    return if (length > maxLength) {
        take(maxLength).trimEnd() + "..."
    } else {
        this
    }
}

