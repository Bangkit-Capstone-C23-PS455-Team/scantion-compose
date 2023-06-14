package com.bangkit.scantion.data.remote.network

import com.bangkit.scantion.data.remote.response.LoginResponse
import com.bangkit.scantion.data.remote.response.LogoutResponse
import com.bangkit.scantion.data.remote.response.RegisterResponse
import com.bangkit.scantion.data.remote.response.UserResponse
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("age") age: Int,
        @Field("city") city: String,
        @Field("province") province: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("user")
    suspend fun getUser(
    ): UserResponse

    @POST("logout")
    suspend fun logoutUser(
    ): LogoutResponse
}