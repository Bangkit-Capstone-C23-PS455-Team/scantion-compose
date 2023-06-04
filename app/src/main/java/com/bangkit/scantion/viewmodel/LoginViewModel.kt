package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.repository.LoginDataStoreRepository
import com.bangkit.scantion.model.UserLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginDataStoreRepository
) : ViewModel() {
    fun saveLoginState(userLog: UserLog) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLoginState(userLog)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }
}