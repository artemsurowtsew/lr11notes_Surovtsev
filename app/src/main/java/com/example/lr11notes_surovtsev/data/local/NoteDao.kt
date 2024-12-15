package com.example.lr11notes_surovtsev.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY creationDate DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY creationDate DESC")
    fun getNotesSortedByDateDesc(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY creationDate ASC")
    fun getNotesSortedByDateAsc(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY creationDate DESC")
    fun searchNotes(query: String): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}
