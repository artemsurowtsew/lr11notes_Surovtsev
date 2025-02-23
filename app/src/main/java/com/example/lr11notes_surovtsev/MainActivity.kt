package com.example.lr11notes_surovtsev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lr11notes_surovtsev.presentation.noteslist.NotesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NotesListFragment())
                .commitNow()
        }
    }
}
