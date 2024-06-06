package com.github.slznvk.notesapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.slznvk.notesapp.domain.dto.Note
import com.github.slznvk.notesapp.domain.dto.Priority
import com.github.slznvk.notesapp.domain.repository.NoteRepository
import com.github.slznvk.notesapp.presentation.model.StateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val data = noteRepository.getAllNote()
    val filteredData = MutableLiveData(listOf<Note>())

    private val _dataState = MutableLiveData(StateModel())
    val dataState: LiveData<StateModel>
        get() = _dataState

    private val _pickedNote = MutableLiveData(empty)
    val pickedNote: LiveData<Note>
        get() = _pickedNote

    fun getNoteById(id: Long) {
        try {
            _pickedNote.postValue(noteRepository.getNoteById(id))
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

    fun removeNoteById(id: Long) {
        try {
            noteRepository.removeNoteById(id)
            _pickedNote.value = empty
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

    fun saveNote() {
        try {
            _pickedNote.value?.let {
                noteRepository.saveNote(it)
            }
            _pickedNote.value = empty
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

    fun edit(note: Note) {
        _pickedNote.value = note
    }

    fun changePriority(priority: Priority) {
        _pickedNote.value?.let { note ->
            if (priority != note.priority) {
                _pickedNote.value = note.copy(priority = priority)
            } else _pickedNote.value = note.copy(priority = Priority.NOT)
        }
    }

    fun changeContent(content: String, time: String, title: String) {
        _pickedNote.value?.let { note ->
            val newContent = content.trim()
            val newTitle = title.trim()
            if (newContent != note.content || newTitle != note.title) {
                _pickedNote.value = note.copy(
                    content = newContent,
                    time = time,
                    title = title.ifBlank {
                        note.title.ifBlank {
                            content.take(
                                min(20, content.length)
                            )
                        }
                    }
                )
            }
        }
    }

    fun cancel() {
        _pickedNote.value = empty
    }

    fun filter(query: String?) {
        val filteredList = mutableListOf<Note>()

        data.value?.forEach {
            if (it.content.lowercase(Locale.ROOT)
                    .contains(query?.lowercase(Locale.ROOT).toString()) ||
                it.title.lowercase(Locale.ROOT)
                    .contains(query?.lowercase(Locale.ROOT).toString())
            )
                filteredList.add(it)
        }
        filteredData.value = filteredList
    }
}


private val empty = Note(id = 0, title = "", content = "", time = "")