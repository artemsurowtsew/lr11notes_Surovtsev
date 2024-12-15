package com.example.lr11notes_surovtsev.presentation.noteslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.lr11notes_surovtsev.R
import com.example.lr11notes_surovtsev.data.local.AppDatabase
import com.example.lr11notes_surovtsev.data.repository.NotesRepositoryImpl
import com.example.lr11notes_surovtsev.databinding.FragmentNotesListBinding
import com.example.lr11notes_surovtsev.domain.usecase.GetNotesUseCase
import com.example.lr11notes_surovtsev.domain.usecase.SearchNotesUseCase
import com.example.lr11notes_surovtsev.presentation.notedetail.NoteDetailFragment
import com.example.lr11notes_surovtsev.presentation.noteedit.NoteEditFragment

class NotesListFragment : Fragment() {

    private lateinit var binding: FragmentNotesListBinding
    private lateinit var viewModel: NotesListViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "notes_db").build()
        val repository = NotesRepositoryImpl(db.noteDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return NotesListViewModel(
                    getNotesUseCase = GetNotesUseCase(repository),
                    searchNotesUseCase = SearchNotesUseCase(repository)
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, factory)[NotesListViewModel::class.java]

        adapter = NotesAdapter { note ->
            val fragment = NoteDetailFragment.newInstance(note.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.filteredNotes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        binding.fabAdd.setOnClickListener {
            val fragment = NoteEditFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}
