package com.example.lr11notes_surovtsev.domain.usecase

import com.example.lr11notes_surovtsev.data.repository.NotesRepository

class GetNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke() = repository.getAllNotes()
}
