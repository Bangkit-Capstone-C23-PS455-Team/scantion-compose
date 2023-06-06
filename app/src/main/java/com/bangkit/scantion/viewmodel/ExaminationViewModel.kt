package com.bangkit.scantion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.database.SkinExamsDao
import com.bangkit.scantion.model.SkinCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExaminationViewModel(
    private val skinExamsDao: SkinExamsDao
) : ViewModel() {

    val skinExams: LiveData<List<SkinCase>> = skinExamsDao.getSkinExams()

    fun deleteSkinExam(skinCase: SkinCase){
        viewModelScope.launch (Dispatchers.IO) {
            skinExamsDao.deleteSkinExam(skinCase)
        }
    }

    fun addSkinExam(skinCase: SkinCase){
        viewModelScope.launch (Dispatchers.IO) {
            skinExamsDao.addSkinExam(skinCase)
        }
    }

    suspend fun getSkinExamById(skinCaseId: String): SkinCase? {
        return skinExamsDao.getSkinExamById(skinCaseId)
    }
}

class ViewModelFactory(
    private val skinExamsDao: SkinExamsDao,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  ExaminationViewModel(
            skinExamsDao = skinExamsDao,
        ) as T
    }
}