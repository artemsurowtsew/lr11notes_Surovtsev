package com.example.lr11notes_surovtsev.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val creationDate: Long
)
