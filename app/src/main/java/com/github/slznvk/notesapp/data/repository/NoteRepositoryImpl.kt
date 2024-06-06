package com.github.slznvk.notesapp.data.repository

import androidx.lifecycle.map
import com.github.slznvk.notesapp.data.dao.NoteDao
import com.github.slznvk.notesapp.data.entity.NoteEntity.Companion.fromDto
import com.github.slznvk.notesapp.data.entity.toDto
import com.github.slznvk.notesapp.domain.dto.Note
import com.github.slznvk.notesapp.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
) : NoteRepository {
    override fun getAllNote() = dao.getAll().map { it.toDto() }

    override fun getNoteById(id: Long): Note = dao.getNoteById(id).toDto()

    override fun saveNote(note: Note) {
        dao.saveNote(fromDto(note))
    }

    override fun removeNoteById(id: Long) = dao.removeById(id)
}