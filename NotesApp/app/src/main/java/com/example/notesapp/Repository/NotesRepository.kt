package com.example.notesapp.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.notesapp.Dao.NotesDao
import com.example.notesapp.Database.NotesDatabase
import com.example.notesapp.Model.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepository(val notesDao: NotesDao) {

    fun getNotes(): LiveData<List<Notes>> = notesDao.getNotes()

    fun getHighNotes(): LiveData<List<Notes>> = notesDao.getHighNotes()

    fun getMediumNotes(): LiveData<List<Notes>> = notesDao.getMediumNotes()

    fun getLowNotes(): LiveData<List<Notes>> = notesDao.getLowNotes()

    suspend fun insertNotes(notes: Notes) {
        withContext(Dispatchers.IO) {
            notesDao.insertNotes(notes)
        }
    }

    suspend fun deleteNotes(id: Int) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNotes(id)
        }
    }

    suspend fun updateNotes(notes: Notes) {
        withContext(Dispatchers.IO) {
            notesDao.updateNotes(notes)
        }
    }
}
