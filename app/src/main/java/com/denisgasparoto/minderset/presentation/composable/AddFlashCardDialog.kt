package com.denisgasparoto.minderset.presentation.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.domain.model.ValidationError
import com.denisgasparoto.minderset.domain.usecase.ValidationKeys
import com.denisgasparoto.minderset.presentation.theme.Dimens

@Composable
internal fun AddFlashCardDialog(
    initialQuestion: String = "",
    initialAnswer: String = "",
    initialCategory: String = "",
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSave: (question: String, answer: String, category: String) -> Unit,
    validate: (String, String) -> Map<String, ValidationError>,
    existingCategories: List<String> = emptyList()
) {
    if (!isOpen) return

    var questionInput by remember { mutableStateOf(initialQuestion) }
    var answerInput by remember { mutableStateOf(initialAnswer) }
    var categoryInput by remember { mutableStateOf(initialCategory) }
    var showValidationError by remember { mutableStateOf(false) }

    val validationErrors = validate(questionInput, answerInput)
    val isInputValid = validationErrors.isEmpty()

    val questionErrorText = mapValidationErrorToText(
        error = validationErrors[ValidationKeys.QUESTION],
        tooShortRes = R.string.validation_question_too_short,
        tooLongRes = R.string.validation_question_too_long,
        minLength = ValidationKeys.MIN_QUESTION_TEXT_LENGTH,
        maxLength = ValidationKeys.MAX_QUESTION_TEXT_LENGTH
    )

    val answerErrorText = mapValidationErrorToText(
        error = validationErrors[ValidationKeys.ANSWER],
        tooShortRes = R.string.validation_answer_too_short,
        tooLongRes = R.string.validation_answer_too_long,
        minLength = ValidationKeys.MIN_ANSWER_TEXT_LENGTH,
        maxLength = ValidationKeys.MAX_ANSWER_TEXT_LENGTH
    )

    AlertDialog(
        onDismissRequest = {
            onDismiss()
            showValidationError = false
        },
        confirmButton = {
            TextButton(onClick = {
                showValidationError = true
                if (isInputValid) {
                    onSave(questionInput.trim(), answerInput.trim(), categoryInput.trim())
                    showValidationError = false
                }
            }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                showValidationError = false
            }) {
                Text(stringResource(R.string.button_cancel))
            }
        },
        title = { Text(stringResource(R.string.add_flash_card_title)) },
        text = {
            Column {
                ValidatedTextField(
                    value = questionInput,
                    onValueChange = { questionInput = it },
                    label = stringResource(R.string.label_question),
                    errorText = if (showValidationError) questionErrorText else null,
                    maxLength = ValidationKeys.MAX_QUESTION_TEXT_LENGTH
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

                ValidatedTextField(
                    value = answerInput,
                    onValueChange = { answerInput = it },
                    label = stringResource(R.string.label_answer),
                    errorText = if (showValidationError) answerErrorText else null,
                    maxLength = ValidationKeys.MAX_ANSWER_TEXT_LENGTH,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = Dimens.TextFieldMinHeight)
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceSmall))

                CategoryInputDropdown(
                    categoryInput = categoryInput,
                    onCategoryChange = { categoryInput = it },
                    existingCategories = existingCategories
                )
            }
        }
    )
}

@Composable
private fun mapValidationErrorToText(
    error: ValidationError?,
    @StringRes tooShortRes: Int,
    @StringRes tooLongRes: Int,
    minLength: Int,
    maxLength: Int
): String? = when (error) {
    ValidationError.QuestionTooShort, ValidationError.AnswerTooShort ->
        stringResource(tooShortRes, minLength)

    ValidationError.QuestionTooLong, ValidationError.AnswerTooLong ->
        stringResource(tooLongRes, maxLength)

    else -> null
}
