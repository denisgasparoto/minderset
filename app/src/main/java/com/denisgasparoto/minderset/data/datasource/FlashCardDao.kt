package com.denisgasparoto.minderset.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.denisgasparoto.minderset.data.model.FlashCardEntity

@Dao
internal interface FlashCardDao {
    @Query("SELECT * FROM FlashCardEntity")
    suspend fun getAll(): List<FlashCardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: FlashCardEntity)

    @Delete
    suspend fun delete(card: FlashCardEntity)
}