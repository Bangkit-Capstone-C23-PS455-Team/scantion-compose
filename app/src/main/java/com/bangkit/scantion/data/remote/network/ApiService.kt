package com.bangkit.scantion.data.remote.network

import com.bangkit.scantion.data.remote.response.RegisterResponse
import com.bangkit.scantion.model.UserReg
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("addUsers")
    fun register(@Body userReg: UserReg?): Call<RegisterResponse>
}