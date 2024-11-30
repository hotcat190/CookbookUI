package com.example.androidcookbook.ui.features.aigen


import android.hardware.biometrics.BiometricManager
import com.example.androidcookbook.domain.model.aigen.AiRecipe
import com.example.androidcookbook.domain.model.aigen.Ingredient
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class UploadResponse(
    val ingredient: List<Ingredient>,
    val quantity: List<String>
)

data class GetReponse(
    val userId: String,
    val bio: String,
    val name: String,
    val avatar: String,
    val totalFollowers: Int,
    val totalFollowing: Int,
)
interface ApiService {
    @Multipart
    @POST("airecipe/uploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<AiRecipe>

}
