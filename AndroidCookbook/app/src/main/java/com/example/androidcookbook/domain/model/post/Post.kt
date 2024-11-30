package com.example.androidcookbook.domain.model.post

import com.example.androidcookbook.domain.model.user.User

data class Post(
    val id: Int,
    val author: User,
    val title: String,
    val description: String,
    val cookTime: Int?,
    val mainImage: String?,
    val totalView: Int,
    val totalComment: Int,
    val totalLike: Int,
    val ingredient: List<String>?,
    val steps: String?,
    val createdAt: String,
)