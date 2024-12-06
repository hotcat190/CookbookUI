package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.AiGenService
import okhttp3.MultipartBody
import javax.inject.Inject

class AiGenRepository @Inject constructor(
    val aiGenService: AiGenService
) {
    suspend fun uploadImage(image: MultipartBody.Part) = aiGenService.uploadImage(image)
}
