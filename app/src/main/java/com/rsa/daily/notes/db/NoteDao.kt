package com.rsa.daily.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rsa.daily.notes.model.NoteModel

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteModel)

    @Update
    suspend fun updateNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<NoteModel>>

    @Query("DELETE FROM note_table")
    suspend fun clearNote()

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNoteById(id: Int)
}