package com.bangkit.scantion.data.remote.network

import com.bangkit.scantion.data.remote.response.RegisterResponse
import com.bangkit.scantion.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("addUsers")
    fun register(@Body user: User?): Call<RegisterResponse>
}