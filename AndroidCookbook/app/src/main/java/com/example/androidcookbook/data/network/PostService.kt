package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.post.PostCreateRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface PostService {
    @POST("posts")
    suspend fun createPost(@Body post: PostCreateRequest)
}