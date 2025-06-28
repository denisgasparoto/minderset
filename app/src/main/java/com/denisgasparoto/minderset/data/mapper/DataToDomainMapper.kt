package com.denisgasparoto.minderset.data.mapper

import com.denisgasparoto.minderset.data.model.FlashCardEntity
import com.denisgasparoto.minderset.domain.model.FlashCard

internal fun FlashCardEntity.toDomain() = FlashCard(id, question, answer, category)