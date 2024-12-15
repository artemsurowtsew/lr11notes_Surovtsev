package com.example.lr11notes_surovtsev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lr11notes_surovtsev.domain.model.Note
import java.util.Date

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val creationDate: Long = Date().time
)

private fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        creationDate = creationDate
    )
}