package com.example.lr11notes_surovtsev.domain.usecase

import com.example.lr11notes_surovtsev.data.repository.NotesRepository
import com.example.lr11notes_surovtsev.domain.model.Note

class InsertNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
