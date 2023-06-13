package com.bangkit.scantion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.util.Constants

@Database(
    entities = [SkinCase::class], version = 1
)
abstract class SkinExamsDatabase : RoomDatabase() {
    abstract fun skinExamsDao(): SkinExamsDao

    companion object {
        @Volatile
        private var INSTANCE: SkinExamsDatabase? = null
        fun getInstance(context: Context): SkinExamsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkinExamsDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}