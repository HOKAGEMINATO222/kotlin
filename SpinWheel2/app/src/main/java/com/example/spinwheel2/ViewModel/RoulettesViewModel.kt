package com.example.spinwheel2.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.bluehomestudio.luckywheel.WheelItem
import com.example.spinwheel2.Database.RoulettesDatabase
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes
import com.example.spinwheel2.R
import com.example.spinwheel2.Repository.RoulettesRepository
import kotlinx.coroutines.launch

class RoulettesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoulettesRepository

    private val _wheelItems = MutableLiveData<List<WheelItem>>()
    val wheelItems: LiveData<List<WheelItem>> get() = _wheelItems

    private val _selectedRoulette = MutableLiveData<Roulettes?>()
    val selectedRoulette: LiveData<Roulettes?> get() = _selectedRoulette

    private val colors = listOf(
        Color.parseColor("#FF0000"),
        Color.parseColor("#FFA500"),
        Color.parseColor("#FFFF00"),
        Color.parseColor("#008000"),
        Color.parseColor("#0000FF"),
        Color.parseColor("#4B0082"),
        Color.parseColor("#8B00FF")
    )

    init {
        val roulettesDao = RoulettesDatabase.getDatabaseInstance(application).myRoulettesDao()
        repository = RoulettesRepository(roulettesDao)
    }


    fun generateDefaultWheelItems() {
        viewModelScope.launch {
            val items = mutableListOf<WheelItem>()

            val size = 1

            val bitmap1 = BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.shoot)
            val scaledBitmap1 = Bitmap.createScaledBitmap(bitmap1, size, size, false)

            val bitmap2 = BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.shoot)
            val scaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, size, size, false)

            items.add(WheelItem(colors[1], scaledBitmap1, " 1"))
            items.add(WheelItem(colors[2], scaledBitmap2, " 2"))
            items.add(WheelItem(colors[3], scaledBitmap2, " 3"))
            items.add(WheelItem(colors[4], scaledBitmap2, " 4"))
            items.add(WheelItem(colors[5], scaledBitmap2, " 5"))

            _wheelItems.value = items
        }
    }

    fun generateWheelItemsFromRouletteId(rouletteId: Int) {
        repository.getItemsByRouletteId(rouletteId).observeForever { itemsFromDb ->
            val size = 1
            val bitmap1 = BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.shoot)
            val scaledBitmap1 = Bitmap.createScaledBitmap(bitmap1, size, size, false)
            val wheelItems = itemsFromDb?.map { item ->
                WheelItem(Color.parseColor(item.color), scaledBitmap1, item.name)
            } ?: emptyList()

            _wheelItems.value = wheelItems
        }
    }

    fun setSelectedRoulette(roulette: Roulettes) {
        _selectedRoulette.value = roulette
    }

    fun getAllRoulettes(): LiveData<List<Roulettes>> = repository.getAllRoulettes()

    fun deleteRoulette(roulette: Roulettes) {
        viewModelScope.launch {
            repository.delete(roulette)
        }
    }

    fun deleteItem(item: Items) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun deleteItemsByRouletteId(rouletteId: Int) {
        viewModelScope.launch {
            repository.deleteItemsByRouletteId(rouletteId)
        }
    }

    fun insertRoulette(roulette: Roulettes) = liveData {
        val id = repository.insertRoulette(roulette)
        emit(id)
    }

    fun insertItems(items: List<Items>) = viewModelScope.launch {
        repository.insertItems(items)
    }

    fun getItemsByRouletteId(rouletteId: Int): LiveData<List<Items>> {
        return repository.getItemsByRouletteId(rouletteId)
    }

    fun updateRoulette(roulette: Roulettes) = viewModelScope.launch {
        repository.updateRoulette(roulette)
    }
}
