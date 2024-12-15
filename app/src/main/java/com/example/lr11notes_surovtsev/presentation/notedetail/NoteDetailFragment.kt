package com.example.lr11notes_surovtsev.presentation.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import android.view.ViewGroup
import com.example.lr11notes_surovtsev.R
import com.example.lr11notes_surovtsev.data.local.AppDatabase
import com.example.lr11notes_surovtsev.data.repository.NotesRepositoryImpl
import com.example.lr11notes_surovtsev.databinding.FragmentNoteDetailBinding
import com.example.lr11notes_surovtsev.domain.usecase.DeleteNoteUseCase
import com.example.lr11notes_surovtsev.presentation.noteedit.NoteEditFragment
import kotlinx.coroutines.launch

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var viewModel: NoteDetailViewModel
    private var noteId: Long = 0

    companion object {
        private const val ARG_NOTE_ID = "arg_note_id"

        fun newInstance(noteId: Long): NoteDetailFragment {
            val fragment = NoteDetailFragment()
            val args = Bundle()
            args.putLong(ARG_NOTE_ID, noteId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = arguments?.getLong(ARG_NOTE_ID) ?: 0
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "notes_db").build()
        val repository = NotesRepositoryImpl(db.noteDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteDetailViewModel(repository, noteId) as T
            }
        }

        viewModel = ViewModelProvider(this, factory)[NoteDetailViewModel::class.java]

        viewModel.note.observe(viewLifecycleOwner) { note ->
            note?.let {
                binding.tvTitle.text = it.title
                binding.tvContent.text = it.content
            }
        }

        binding.btnEdit.setOnClickListener {
            val fragment = NoteEditFragment.newInstance(noteId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "notes_db").build()
            val repository = NotesRepositoryImpl(db.noteDao())
            lifecycleScope.launch {
                viewModel.note.value?.let {
                    DeleteNoteUseCase(repository).invoke(it)
                    parentFragmentManager.popBackStack()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
