package com.github.slznvk.notesapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.slznvk.notesapp.domain.dto.Note
import com.github.slznvk.notesapp.domain.dto.Priority

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val content: String,
    val time: String,
    val priority: Priority
) {
    fun toDto() = Note(
        id = id,
        title = title,
        content = content,
        time = time,
        priority = priority
    )

    companion object {
        fun fromDto(dto: Note) = NoteEntity(
            id = dto.id,
            title = dto.title,
            content = dto.content,
            time = dto.time,
            priority = dto.priority
        )
    }
}

fun List<NoteEntity>.toDto(): List<Note> = map(NoteEntity::toDto)