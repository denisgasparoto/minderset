package com.denisgasparoto.minderset.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.denisgasparoto.minderset.data.datasource.FlashCardDao
import com.denisgasparoto.minderset.data.model.FlashCardEntity

@Database(entities = [FlashCardEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao
}