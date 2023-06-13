package com.bangkit.scantion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.database.SkinExamsDao
import com.bangkit.scantion.model.SkinCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExaminationViewModel @Inject constructor(
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