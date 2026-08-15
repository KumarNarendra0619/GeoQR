package com.geolens.camera.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptureDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: CaptureRecord)

    @Query("SELECT * FROM captures ORDER BY capturedAtEpochMs DESC")
    fun observeAll(): Flow<List<CaptureRecord>>
}
