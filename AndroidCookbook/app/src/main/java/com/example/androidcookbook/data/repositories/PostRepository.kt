package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.PostService
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import com.example.androidcookbook.domain.model.post.SendCommentRequest
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postService: PostService
) {
    suspend fun createPost(post: PostCreateRequest) = postService.createPost(post)

    suspend fun getPost(postId: Int) = postService.getPost(postId)

    suspend fun queryPostLike(postId: Int) = postService.queryPostLike(postId)

    suspend fun likePost(id: Int) = postService.likePost(id)

    suspend fun unlikePost(id: Int) = postService.unlikePost(id)

    suspend fun getComments(postId: Int, page: Int) = postService.getComments(postId, page)

    suspend fun sendComment(postId: Int, request: SendCommentRequest) = postService.sendComment(postId, request)
}