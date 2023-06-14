package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.preference.login.LoginDataStoreRepository
import com.bangkit.scantion.data.repository.AuthRepository
import com.bangkit.scantion.model.UserLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LoginDataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun clearDatastore() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }

    fun logoutUser() = authRepository.logoutUser()

    private val _userLog: MutableStateFlow<UserLog?> = MutableStateFlow(null)
    val userLog: StateFlow<UserLog?> = _userLog

    init {
        viewModelScope.launch {
            repository.getUserLogin().collect {
                if (it != null) {
                    _userLog.value = it
                }
            }
        }
    }
}