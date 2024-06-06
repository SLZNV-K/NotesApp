package com.github.slznvk.notesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.slznvk.notesapp.data.dao.NoteDao
import com.github.slznvk.notesapp.data.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}