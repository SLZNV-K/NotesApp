package com.github.slznvk.notesapp.domain.dto

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val time: String,
    val priority: Priority = Priority.NOT
)