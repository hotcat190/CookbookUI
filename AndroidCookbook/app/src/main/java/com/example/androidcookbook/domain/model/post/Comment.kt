package com.example.androidcookbook.domain.model.post

import com.example.androidcookbook.domain.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int = 0,
    val user: User = User(),
    val content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    val createdAt: String = "01/01/2024",
    val likes: Int = 0,
    val replies: List<Comment> = emptyList(),
    val isLiked: Boolean = false,
)
