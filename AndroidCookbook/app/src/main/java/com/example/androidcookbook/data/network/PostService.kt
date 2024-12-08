package com.example.androidcookbook.data.network

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
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostService {
    @POST("posts")
    suspend fun createPost(@Body post: PostCreateRequest): ApiResponse<PostCreateResponse>

    @GET("posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Int): ApiResponse<Post>

    @GET("posts/{postId}/like")
    suspend fun queryPostLike(@Path("postId") postId: Int): ApiResponse<SuccessMessageBody> // TODO

    @POST("like/{postId}")
    suspend fun likePost(@Path("postId") postId: Int): ApiResponse<SuccessMessageBody>

    @DELETE("like/{postId}")
    suspend fun unlikePost(@Path("postId") postId: Int): ApiResponse<SuccessMessageBody>

    @GET("comment/{postId}/{page}")
    suspend fun getComments(@Path("postId") postId: Int, @Path("page") page: Int): ApiResponse<GetCommentResponse>

    @POST("comment/{postId}")
    suspend fun sendComment(@Path("postId") postId: Int, @Body request: SendCommentRequest): ApiResponse<SuccessMessageBody>

    @PUT("comment/{commentId}")
    suspend fun editComment(@Path("commentId") commentId: Int, @Body request: SendCommentRequest): ApiResponse<SuccessMessageBody>

    @DELETE("comment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Int): ApiResponse<SuccessMessageBody>

    @POST("comment/{commentId}/like")
    suspend fun likeComment(@Path("commentId") commentId: Int): ApiResponse<SuccessMessageBody>

    @DELETE("comment/{commentId}/like")
    suspend fun unlikeComment(@Path("commentId") commentId: Int): ApiResponse<SuccessMessageBody>
}