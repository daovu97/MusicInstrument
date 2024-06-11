package com.musical.instrument.simulator.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "audio_files")
data class AudioFile(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "filePath")
    var filePath: String? = null,

    @ColumnInfo(name = "createdAt")
    var createdAt: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "category")
    var category: Int = TYPE_AUDIO,

    @ColumnInfo(name = "json_string")
    var jsonString: String? = null,
) : Serializable {
    companion object {
        const val TYPE_AUDIO = 1
        const val TYPE_PIANO = 2
    }
}

