package com.github.slznvk.notesapp.domain.repository

import androidx.lifecycle.LiveData
import com.github.slznvk.notesapp.domain.dto.Note

interface NoteRepository {
    fun getAllNote(): LiveData<List<Note>>
    fun getNoteById(id: Long): Note
    fun saveNote(note: Note)
    fun removeNoteById(id: Long)
}