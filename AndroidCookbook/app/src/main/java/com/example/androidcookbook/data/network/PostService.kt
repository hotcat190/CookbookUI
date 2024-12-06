package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import com.example.androidcookbook.domain.model.post.PostCreateResponse
import com.example.androidcookbook.domain.network.SuccessMessageBody
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @POST("posts")
    suspend fun createPost(@Body post: PostCreateRequest): ApiResponse<PostCreateResponse>

    @GET("posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Int): ApiResponse<Post>
}