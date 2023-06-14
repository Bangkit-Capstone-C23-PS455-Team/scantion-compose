package com.bangkit.scantion.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.scantion.data.remote.network.ApiConfig
import com.bangkit.scantion.data.remote.network.ApiService
import com.bangkit.scantion.data.remote.response.LoginResponse
import com.bangkit.scantion.data.remote.response.LogoutResponse
import com.bangkit.scantion.data.remote.response.RegisterResponse
import com.bangkit.scantion.data.remote.response.UserResponse
import com.bangkit.scantion.util.Resource

class AuthRepository (context: Context) {
    private val apiService: ApiService = ApiConfig.getApiService(context)

    fun registerUser(name: String, email: String, password: String, age: Int, province: String, city: String): LiveData<Resource<RegisterResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.registerUser(name, email, password, age, city, province)
            emit(Resource.Success(response))
            Log.e("RegisterViewModel", "postRegister: register success")
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "postRegister: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun loginUser(email: String, password: String): LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.loginUser(email, password)
            emit(Resource.Success(response))
            Log.e("LoginViewModel", "postLogin: success")
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun getUser(): LiveData<Resource<UserResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getUser()
            emit(Resource.Success(response))
            Log.e("LoginViewModel", "getUser: success")
        } catch (e: Exception) {
            Log.e("LoginViewModel", "getUser: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun logoutUser(): LiveData<Resource<LogoutResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.logoutUser()
            emit(Resource.Success(response))
            Log.e("profile", "logout: success")
        } catch (e: Exception) {
            Log.e("profile", "logout: ${e.message.toString()}")
            emit(Resource.Error(e.message.toString()))
        }
    }
}