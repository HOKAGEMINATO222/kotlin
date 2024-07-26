package com.example.notesapp.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.Model.Notes


@Dao
interface NotesDao {
    @Query(value = "SELECT * From Notes")
    fun getNotes(): LiveData<List<Notes>>

    @Query(value = "SELECT * From Notes WHERE priority = 3")
    fun getHighNotes(): LiveData<List<Notes>>

    @Query(value = "SELECT * From Notes WHERE priority = 2")
    fun getMediumNotes(): LiveData<List<Notes>>

    @Query(value = "SELECT * From Notes WHERE priority = 1")
    fun getLowNotes(): LiveData<List<Notes>>

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: Notes )

    @Query("DELETE FROM Notes WHERE id = :id")
    fun deleteNotes(id: Int)


    @Update
    fun updateNotes( notes: Notes)
}