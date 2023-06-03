package com.bangkit.scantion.data.repository

import com.bangkit.scantion.data.remote.network.ApiConfig
import com.bangkit.scantion.data.remote.network.ApiService
import com.bangkit.scantion.data.remote.response.RegisterResponse
import com.bangkit.scantion.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService: ApiService = ApiConfig.getApiService()

    fun registerUser(user: User, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val call: Call<RegisterResponse> = apiService.register(user)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse: RegisterResponse? = response.body()
                    val result: String? = registerResponse?.result
                    if (result != null) {
                        onSuccess(result)
                    } else {
                        onError("Invalid response")
                    }
                } else {
                    onError("API error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onError("API call failed: ${t.message}")
            }
        })
    }
}