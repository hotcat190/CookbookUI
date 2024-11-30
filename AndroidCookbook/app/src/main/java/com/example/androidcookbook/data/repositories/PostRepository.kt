package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.PostService
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postService: PostService
) {
    suspend fun createPost(post: PostCreateRequest) = postService.createPost(post)
}