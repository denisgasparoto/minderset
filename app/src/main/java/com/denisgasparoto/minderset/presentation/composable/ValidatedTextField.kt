package com.denisgasparoto.minderset.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisgasparoto.minderset.R
import com.denisgasparoto.minderset.presentation.theme.Dimens

@Composable
internal fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String?,
    maxLength: Int,
    maxLines: Int = 3,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) onValueChange(it)
        },
        label = { Text(label) },
        isError = errorText != null,
        supportingText = {
            if (errorText != null) {
                Text(errorText, color = MaterialTheme.colorScheme.error)
            } else {
                Text(stringResource(R.string.char_count, value.length, maxLength))
            }
        },
        maxLines = maxLines,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.TextFieldMinHeight, max = Dimens.TextFieldMaxHeight)
            .verticalScroll(rememberScrollState())
    )
}
