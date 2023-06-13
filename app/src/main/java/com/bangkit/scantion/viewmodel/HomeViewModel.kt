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
class HomeViewModel @Inject constructor(
    private val repository: LoginDataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getUser() = authRepository.getUser()

    fun saveUser(userLog: UserLog) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(userLog)
        }
    }

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