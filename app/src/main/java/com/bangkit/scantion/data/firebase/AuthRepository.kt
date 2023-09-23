package com.bangkit.scantion.data.firebase

import androidx.lifecycle.LiveData
import com.bangkit.scantion.util.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun login(email: String, password: String): LiveData<Resource<FirebaseUser>>
    fun signup(name: String, email: String, password: String): LiveData<Resource<FirebaseUser>>
    fun logout()
}