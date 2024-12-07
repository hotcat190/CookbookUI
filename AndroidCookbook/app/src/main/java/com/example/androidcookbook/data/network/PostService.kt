package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.post.Comment
import com.example.androidcookbook.domain.model.post.GetCommentResponse
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import com.example.androidcookbook.domain.model.post.PostCreateResponse
import com.example.androidcookbook.domain.model.post.SendCommentRequest
import com.example.androidcookbook.domain.network.SuccessMessageBody
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @POST("posts")
    suspend fun createPost(@Body post: PostCreateRequest): ApiResponse<PostCreateResponse>

    @GET("posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Int): ApiResponse<Post>

    @GET("posts/{postId}/like")
    suspend fun queryPostLike(@Path("postId") postId: Int): ApiResponse<SuccessMessageBody>

    @POST("like/{id}")
    suspend fun likePost(@Path("id") id: Int): ApiResponse<SuccessMessageBody>

    @DELETE("like/{id}")
    suspend fun unlikePost(@Path("id") id: Int): ApiResponse<SuccessMessageBody>

    @GET("comment/{postId}/{page}")
    suspend fun getComments(@Path("postId") postId: Int, @Path("page") page: Int): ApiResponse<GetCommentResponse>

    @POST("comment/{postId}")
    suspend fun sendComment(@Path("postId") postId: Int, @Body request: SendCommentRequest): ApiResponse<SuccessMessageBody>
}