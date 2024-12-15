package com.example.lr11notes_surovtsev.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.lr11notes_surovtsev.data.local.NoteDao
import com.example.lr11notes_surovtsev.data.local.NoteEntity
import com.example.lr11notes_surovtsev.data.remote.RetrofitInstance
import com.example.lr11notes_surovtsev.domain.model.Note
import com.example.lr11notes_surovtsev.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val noteDao: NoteDao
) : NotesRepository {

    override fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getSortedNotesByDateDesc(): LiveData<List<Note>> {
        return noteDao.getNotesSortedByDateDesc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getSortedNotesByDateAsc(): LiveData<List<Note>> {
        return noteDao.getNotesSortedByDateAsc().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    override suspend fun refreshNotesFromRemote() {
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.api.fetchNotes()
            if (response.isSuccessful) {
                response.body()?.let { notes ->
                    // Оновлення БД
                    for (n in notes) {
                        noteDao.insertNote(n.toEntity())
                    }
                }
            }
        }
    }

    private fun NoteEntity.toDomain(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            creationDate = creationDate
        )
    }

    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            creationDate = creationDate
        )
    }
}
