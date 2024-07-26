package com.example.spinwheel2.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes

@Dao
interface RoulettesDao {

    // Roulettes
    @Query("SELECT * FROM Roulettes")
    fun getAllRoulettes(): LiveData<List<Roulettes>>

    @Query("SELECT * FROM Roulettes WHERE id = :id")
    fun getRouletteById(id: Int): Roulettes?

    @Insert
    suspend fun insertRoulette(roulette: Roulettes): Long

    @Update
    suspend fun updateRoulette(roulette: Roulettes)

    @Delete
    suspend fun delete(roulette: Roulettes)

    // Items
    @Query("SELECT * FROM items WHERE rouletteId = :rouletteId")
    fun getItemsByRouletteId(rouletteId: Int): LiveData<List<Items>>


    @Insert
    suspend fun insertItems(items: List<Items>): List<Long>

    @Query("DELETE FROM Items WHERE id = :id")
    suspend fun deleteItemsById(id: Int)

    @Delete
    suspend fun deleteItem(item: Items)

    @Query("DELETE FROM items WHERE rouletteId = :rouletteId")
    suspend fun deleteItemsByRouletteId(rouletteId: Int)
}
