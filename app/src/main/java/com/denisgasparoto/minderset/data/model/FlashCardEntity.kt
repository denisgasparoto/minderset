package com.denisgasparoto.minderset.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class FlashCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String
)