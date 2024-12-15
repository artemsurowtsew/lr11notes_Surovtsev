package com.example.lr11notes_surovtsev.data.remote

import retrofit2.Response
import retrofit2.http.GET
import com.example.lr11notes_surovtsev.domain.model.Note

interface ApiService {
    @GET("notes")
    suspend fun fetchNotes(): Response<List<Note>>
}
