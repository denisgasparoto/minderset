package com.denisgasparoto.minderset.domain.mapper

import com.denisgasparoto.minderset.data.model.FlashCardEntity
import com.denisgasparoto.minderset.domain.model.FlashCard

internal fun FlashCard.toEntity() = FlashCardEntity(id, question, answer)