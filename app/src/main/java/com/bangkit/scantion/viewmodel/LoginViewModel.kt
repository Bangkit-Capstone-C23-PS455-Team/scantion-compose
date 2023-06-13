package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.preference.login.LoginDataStoreRepository
import com.bangkit.scantion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginDataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    fun loginUser(email: String, password: String) = authRepository.loginUser(email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveToken(token)
        }
    }
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }
}