package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.image.ImageResponseBody
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @POST("uploadImage")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ApiResponse<ImageResponseBody>
}