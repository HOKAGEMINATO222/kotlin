package com.example.notesapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Database.NotesDatabase
import com.example.notesapp.Model.Notes
import com.example.notesapp.Repository.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NotesRepository


    init {
        val notesDao = NotesDatabase.getDatabaseInstance(application).myNotesDao()
        repository = NotesRepository(notesDao)

    }



    fun addNotes(notes: Notes) = viewModelScope.launch {
        repository.insertNotes(notes)
    }

    fun getNotes() : LiveData<List<Notes>> = repository.getNotes()

    fun getHighNotes(): LiveData<List<Notes>> = repository.getHighNotes()

    fun getMediumNotes(): LiveData<List<Notes>> = repository.getMediumNotes()

    fun getLowNotes(): LiveData<List<Notes>> = repository.getLowNotes()

    fun deleteNotes(id: Int) = viewModelScope.launch {
        repository.deleteNotes(id)
    }

    fun updateNotes(notes: Notes) = viewModelScope.launch {
        repository.updateNotes(notes)
    }
}
