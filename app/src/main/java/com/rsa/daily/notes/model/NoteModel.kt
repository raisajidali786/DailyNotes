package com.rsa.daily.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String?,
    val description: String?,
    val time: String?,
    val date: String?,
)