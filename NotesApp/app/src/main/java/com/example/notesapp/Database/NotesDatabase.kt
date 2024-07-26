package com.example.notesapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.Dao.NotesDao
import com.example.notesapp.Model.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)

//triển khai một mẫu Singleton để đảm bảo rằng chỉ có một thể hiện của NotesDatabase tồn tại trong suốt vòng đời của ứng dụng.
abstract class NotesDatabase : RoomDatabase(){

    abstract fun myNotesDao() : NotesDao

    companion object{
        //@Volatile để đảm bảo tính nhất quán khi truy cập từ nhiều luồng khác nhau
        @Volatile
        var INSTANCE : NotesDatabase? = null

        //// Phương thức để lấy thể hiện duy nhất của cơ sở dữ liệu
        fun getDatabaseInstance( context: Context) : NotesDatabase{
            val tmpInstance = INSTANCE
            if( tmpInstance != null){
                return tmpInstance
            }

            synchronized(this){
                val roomDatabaseInstance = Room.databaseBuilder(context, NotesDatabase::class.java, "Notes").fallbackToDestructiveMigration().build()
                INSTANCE = roomDatabaseInstance
                return roomDatabaseInstance
            }
        }
    }
}