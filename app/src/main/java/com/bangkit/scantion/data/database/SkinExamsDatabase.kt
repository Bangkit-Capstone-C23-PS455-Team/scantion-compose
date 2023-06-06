package com.bangkit.scantion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.scantion.model.SkinCase

@Database(
    entities = [SkinCase::class], version = 1
)
abstract class SkinExamsDatabase : RoomDatabase() {
    abstract fun SkinExamsDao(): SkinExamsDao
}