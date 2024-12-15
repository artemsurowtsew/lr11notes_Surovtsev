package com.example.lr11notes_surovtsev.presentation.notedetail

import androidx.lifecycle.*
import com.example.lr11notes_surovtsev.data.repository.NotesRepository
import com.example.lr11notes_surovtsev.domain.model.Note
import kotlinx.coroutines.launch

class NoteDetailViewModel(private val repository: NotesRepository, private val noteId: Long) : ViewModel() {
    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?> get() = _note

    init {
        viewModelScope.launch {
            _note.value = repository.getNoteById(noteId)
        }
    }
}
