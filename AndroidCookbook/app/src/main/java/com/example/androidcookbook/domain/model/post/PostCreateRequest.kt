package com.example.androidcookbook.domain.model.post

data class PostCreateRequest(
    val title: String,
    val description: String,
    val mainImage: String?,
    val cookTime: Int?,
    val ingredient: List<String>?,
    val steps: String?,
)
