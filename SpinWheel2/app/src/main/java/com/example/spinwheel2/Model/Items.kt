package com.example.spinwheel2.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Items",
    foreignKeys = [ForeignKey(entity = Roulettes::class, parentColumns = ["id"], childColumns = ["rouletteId"], onDelete = ForeignKey.CASCADE)])
data class Items(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "Name") var name: String,
    @ColumnInfo(name = "Color") var color: String,
    @ColumnInfo(name = "rouletteId") var rouletteId: Int
)
