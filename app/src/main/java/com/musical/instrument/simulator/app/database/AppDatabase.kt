package com.musical.instrument.simulator.app.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.musical.instrument.simulator.app.model.AudioFile

@Database(
    entities = [AudioFile::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun audioDao(): AudioDao
}
