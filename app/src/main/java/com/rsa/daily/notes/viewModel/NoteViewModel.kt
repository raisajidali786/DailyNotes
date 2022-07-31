package com.rsa.daily.notes.viewModel

import androidx.lifecycle.ViewModel
import com.rsa.daily.notes.controller.NoteRepository
import com.rsa.daily.notes.model.NoteModel

class NoteViewModel(
    private val repository: NoteRepository
): ViewModel() {

    suspend fun insertNote(note: NoteModel) = repository.insertNote(note)

    suspend fun updateNote(note: NoteModel) = repository.updateNote(note)

    suspend fun deleteNote(note: NoteModel) = repository.deleteNote(note)

    suspend fun deleteNoteById(id: Int) = repository.deleteNoteById(id)

    suspend fun clearNote() = repository.clearNote()

    fun getAllNotes() = repository.getAllNotes()
}