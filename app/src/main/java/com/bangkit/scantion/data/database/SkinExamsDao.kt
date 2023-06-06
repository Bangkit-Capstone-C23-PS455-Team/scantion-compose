package com.bangkit.scantion.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.util.Constants

@Dao
interface SkinExamsDao {
    @Query("SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.TABLE_NAME}.id=:id")
    suspend fun getSkinExamById(id: String): SkinCase?

    @Query("SELECT * FROM ${Constants.TABLE_NAME} ORDER BY dateCreated DESC")
    fun getSkinExams(): LiveData<List<SkinCase>>

    @Delete
    fun deleteSkinExam(skinCase: SkinCase): Int

    @Insert
    fun addSkinExam(skinCase: SkinCase)
}