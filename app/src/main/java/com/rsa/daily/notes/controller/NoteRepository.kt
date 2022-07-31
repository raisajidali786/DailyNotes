package com.rsa.daily.notes.controller

import androidx.lifecycle.LiveData
import com.rsa.daily.notes.db.NoteDatabase
import com.rsa.daily.notes.model.NoteModel

class NoteRepository(
    private val noteDatabase: NoteDatabase
) {

    suspend fun insertNote(note: NoteModel) = noteDatabase.getNoteDao().insertNote(note)

    suspend fun updateNote(note: NoteModel) = noteDatabase.getNoteDao().updateNote(note)

    suspend fun deleteNote(note: NoteModel) = noteDatabase.getNoteDao().deleteNote(note)

    suspend fun deleteNoteById(id: Int) = noteDatabase.getNoteDao().deleteNoteById(id)

    suspend fun clearNote() = noteDatabase.getNoteDao().clearNote()

    fun getAllNotes(): LiveData<List<NoteModel>> = noteDatabase.getNoteDao().getAllNotes()
}