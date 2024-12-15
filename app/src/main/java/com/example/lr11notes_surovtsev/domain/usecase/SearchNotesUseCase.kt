package com.example.lr11notes_surovtsev.domain.usecase

import com.example.lr11notes_surovtsev.data.repository.NotesRepository

class SearchNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke(query: String) = repository.searchNotes(query)
}
