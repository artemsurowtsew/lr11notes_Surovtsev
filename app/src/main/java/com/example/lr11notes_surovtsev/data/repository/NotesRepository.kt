
package com.example.lr11notes_surovtsev.data.repository

import androidx.lifecycle.LiveData
import com.example.lr11notes_surovtsev.domain.model.Note

interface NotesRepository {
    fun getAllNotes(): LiveData<List<Note>>
    fun getSortedNotesByDateDesc(): LiveData<List<Note>>
    fun getSortedNotesByDateAsc(): LiveData<List<Note>>
    fun searchNotes(query: String): LiveData<List<Note>>

    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)

    suspend fun refreshNotesFromRemote()
}
