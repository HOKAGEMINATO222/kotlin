package com.example.notesapp.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity( tableName = "Notes")
class Notes (
    @PrimaryKey( autoGenerate = true)
    var id : Int? = null,
    @ColumnInfo(name = "Title")
    var title: String,
    @ColumnInfo( name = "SubTitle")
    var subTitle: String,
    @ColumnInfo( name = "Notes")
    var notes: String,
    @ColumnInfo( name = "Date")
    var date: String,
    @ColumnInfo( name = "Priority")
    var priority: String
) : Serializable