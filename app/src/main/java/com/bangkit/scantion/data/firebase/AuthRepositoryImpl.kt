package com.bangkit.scantion.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.scantion.data.firebase.utils.await
import com.bangkit.scantion.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun login(email: String, password: String): LiveData<Resource<FirebaseUser>> = liveData {
        emit(Resource.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result.user!!))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun signup(name: String, email: String, password: String): LiveData<Resource<FirebaseUser>> = liveData {
        emit(Resource.Loading)
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            emit(Resource.Success(result.user!!))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}