package com.rsa.daily.notes.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(NoteRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.d("error__", "error : " + e.message.toString())
        }
        return super.create(modelClass)
    }
}