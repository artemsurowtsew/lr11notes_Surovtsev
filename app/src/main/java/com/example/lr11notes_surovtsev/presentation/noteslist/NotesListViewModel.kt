package com.example.lr11notes_surovtsev.presentation.noteslist

import androidx.lifecycle.*
import com.example.lr11notes_surovtsev.domain.model.Note
import com.example.lr11notes_surovtsev.domain.usecase.GetNotesUseCase
import com.example.lr11notes_surovtsev.domain.usecase.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {

    private val searchQuery = MutableLiveData<String>("")

    private val allNotes: LiveData<List<Note>> = getNotesUseCase()

    val filteredNotes: LiveData<List<Note>> = searchQuery.switchMap { query ->
        if (query.isNullOrBlank()) {
            allNotes
        } else {
            searchNotesUseCase(query)
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
