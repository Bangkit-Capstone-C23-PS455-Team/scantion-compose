package com.bangkit.scantion.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.scantion.data.firebase.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun login(email: String, password: String) = repository.login(email, password)

    fun signup(name: String, email: String, password: String) = repository.signup(name, email, password)

    fun logout() {
        repository.logout()
    }
}