package com.github.slznvk.notesapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.slznvk.notesapp.data.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getNoteById(id: Long): NoteEntity

    @Upsert
    fun saveNote(note: NoteEntity): Long

    @Query("DELETE FROM NoteEntity WHERE id = :id")
    fun removeById(id: Long)
}