package com.example.spinwheel2.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Roulettes")
data class Roulettes(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "Name") var name: String,
    @ColumnInfo(name = "Tags") var tags: String
)  : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
