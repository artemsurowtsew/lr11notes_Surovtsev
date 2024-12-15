package com.example.lr11notes_surovtsev.presentation.noteedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.lr11notes_surovtsev.data.local.AppDatabase
import com.example.lr11notes_surovtsev.data.repository.NotesRepositoryImpl
import com.example.lr11notes_surovtsev.databinding.FragmentNoteEditBinding
import kotlinx.coroutines.launch

class NoteEditFragment : Fragment() {

    private lateinit var binding: FragmentNoteEditBinding
    private lateinit var viewModel: NoteEditViewModel
    private var noteId: Long = 0

    companion object {
        private const val ARG_NOTE_ID = "arg_note_id"
        fun newInstance(noteId: Long = 0): NoteEditFragment {
            val fragment = NoteEditFragment()
            val args = Bundle()
            args.putLong(ARG_NOTE_ID, noteId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = arguments?.getLong(ARG_NOTE_ID) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNoteEditBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "notes_db").build()
        val repository = NotesRepositoryImpl(db.noteDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return NoteEditViewModel(repository, noteId.takeIf { it > 0 }) as T
            }
        }

        viewModel = ViewModelProvider(this, factory)[NoteEditViewModel::class.java]

        viewModel.note.observe(viewLifecycleOwner) { note ->
            note?.let {
                binding.etTitle.setText(it.title)
                binding.etContent.setText(it.content)
            }
        }

        binding.btnSave.setOnClickListener {
            saveNote()
        }

        binding.etContent.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNote()
                true
            } else {
                false
            }
        }

        return binding.root
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()

        lifecycleScope.launch {
            viewModel.saveNote(title, content)
            parentFragmentManager.popBackStack()
        }
    }
}
