package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.repository.LoginDataStoreRepository
import com.bangkit.scantion.model.UserLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: LoginDataStoreRepository
) : ViewModel() {

    private val _userLog: MutableStateFlow<UserLog?> = MutableStateFlow(null)
    val userLog: StateFlow<UserLog?> = _userLog

    init {
        viewModelScope.launch {
            repository.readLoginState().collect {
                if (it != null) {
                    _userLog.value = it
                }
            }
        }
    }
}