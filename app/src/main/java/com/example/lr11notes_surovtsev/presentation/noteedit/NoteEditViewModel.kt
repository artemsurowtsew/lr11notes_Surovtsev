package com.example.lr11notes_surovtsev.presentation.noteedit

import androidx.lifecycle.*
import com.example.lr11notes_surovtsev.data.repository.NotesRepository
import com.example.lr11notes_surovtsev.domain.model.Note
import kotlinx.coroutines.launch

class NoteEditViewModel(private val repository: NotesRepository, private val noteId: Long?) : ViewModel() {
    private val _note = MutableLiveData<Note?>(null)
    val note: LiveData<Note?> get() = _note

    init {
        if (noteId != null && noteId > 0) {
            viewModelScope.launch {
                _note.value = repository.getNoteById(noteId)
            }
        }
    }

    fun saveNote(title: String, content: String) {
        viewModelScope.launch {
            if (noteId == null || noteId == 0L) {
                repository.insertNote(Note(title = title, content = content, creationDate = System.currentTimeMillis()))
            } else {
                val updatedNote = note.value?.copy(
                    title = title,
                    content = content
                ) ?: Note(id = noteId, title = title, content = content, creationDate = System.currentTimeMillis())
                repository.updateNote(updatedNote)
            }
        }
    }
}
