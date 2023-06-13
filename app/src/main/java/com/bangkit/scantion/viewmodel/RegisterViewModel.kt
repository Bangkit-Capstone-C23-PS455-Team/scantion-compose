package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.scantion.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (private val authRepository: AuthRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = authRepository.registerUser(name, email, password, 0, "", "",)
}