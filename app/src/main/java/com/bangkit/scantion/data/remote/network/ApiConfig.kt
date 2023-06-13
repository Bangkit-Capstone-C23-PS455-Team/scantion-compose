package com.bangkit.scantion.data.remote.network

import android.content.Context
import android.util.Log
import com.bangkit.scantion.BuildConfig
import com.bangkit.scantion.data.preference.login.LoginDataStoreRepository
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val BASE_URL = BuildConfig.BASE_URL

        fun getApiService(context: Context): ApiService {
            val datastore = LoginDataStoreRepository(context)

            val authInterceptor = Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", "multipart/form-data")

                val token = runBlocking {
                    datastore.getToken().firstOrNull()
                }

                Log.d("token", "$token")

                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}