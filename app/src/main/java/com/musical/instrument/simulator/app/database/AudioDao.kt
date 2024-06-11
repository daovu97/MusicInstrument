package com.musical.instrument.simulator.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.musical.instrument.simulator.app.model.AudioFile

@Dao
interface AudioDao {
    @Query("SELECT * FROM audio_files")
    suspend fun getAll(): List<AudioFile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(data: AudioFile)

    @Delete
    suspend fun delete(data: AudioFile)
}
