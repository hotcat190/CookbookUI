package com.example.androidcookbook.ui.features.aigen

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://cookbookbe.onrender.com/" // Sử dụng 10.0.2.2 cho emulator

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor{ chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjMxLCJ1c2VybmFtZSI6ImhvYXByaTEyMyIsInJvbGVzIjpudWxsLCJpYXQiOjE3MzI5MjM3MTUsImV4cCI6MTczMjkyNzMxNX0.-49F7heQjEnZRKL14sViM_ETE0A67YhPI2Z8vEz0FQg") // Attach token
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}