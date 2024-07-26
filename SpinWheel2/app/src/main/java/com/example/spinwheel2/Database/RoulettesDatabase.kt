package com.example.spinwheel2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spinwheel2.Dao.RoulettesDao
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes

@Database(entities = [Roulettes::class, Items::class], version = 2, exportSchema = false)
abstract class RoulettesDatabase : RoomDatabase() {

    abstract fun myRoulettesDao(): RoulettesDao

    companion object {
        @Volatile
        private var INSTANCE: RoulettesDatabase? = null

        fun getDatabaseInstance(context: Context): RoulettesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoulettesDatabase::class.java,
                    "Roulettes.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Thực hiện migration nếu cần
                database.execSQL("ALTER TABLE Roulettes ADD COLUMN new_column TEXT")
            }
        }
    }
}
