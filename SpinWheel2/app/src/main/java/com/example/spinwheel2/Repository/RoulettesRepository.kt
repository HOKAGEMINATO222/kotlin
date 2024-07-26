package com.example.spinwheel2.Repository

import androidx.lifecycle.LiveData
import com.example.spinwheel2.Dao.RoulettesDao
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoulettesRepository(private val roulettesDao: RoulettesDao) {

    fun getAllRoulettes(): LiveData<List<Roulettes>> = roulettesDao.getAllRoulettes()

    suspend fun getRouletteById(id: Int): Roulettes? {
        return withContext(Dispatchers.IO) {
            roulettesDao.getRouletteById(id)
        }
    } fun getItemsByRouletteId(rouletteId: Int): LiveData<List<Items>> {
        return roulettesDao.getItemsByRouletteId(rouletteId)

       }


    suspend fun insertRoulette(roulette: Roulettes): Long {
        return withContext(Dispatchers.IO) {
            roulettesDao.insertRoulette(roulette)
        }
    }

    suspend fun insertItems(items: List<Items>): List<Long> {
        return withContext(Dispatchers.IO) {
            roulettesDao.insertItems(items)
        }
    }

    suspend fun deleteItem(item: Items) {
        roulettesDao.deleteItem(item)
    }


    suspend fun delete(roulette: Roulettes) {
        roulettesDao.delete(roulette)
    }

    suspend fun deleteItemsByRouletteId(rouletteId: Int) {
        roulettesDao.deleteItemsByRouletteId(rouletteId)
    }

    suspend fun updateRoulette(roulette: Roulettes) {
        withContext(Dispatchers.IO) {
            roulettesDao.updateRoulette(roulette)
        }
    }
}
