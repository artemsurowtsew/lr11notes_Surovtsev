package com.example.lr11notes_surovtsev.domain.usecase


import com.example.lr11notes_surovtsev.domain.model.Note
import com.example.lr11notes_surovtsev.data.repository.NotesRepository

class DeleteNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}
