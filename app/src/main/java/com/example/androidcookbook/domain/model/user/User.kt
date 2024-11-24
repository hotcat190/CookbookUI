package com.example.androidcookbook.domain.model.user

data class User(
    val userId: Int = 0,
    val bio: String = "",
    val name: String = "Guest",
    val avatar: String? = null,
    val totalFollowers: Int = 0,
    val totalFollowing: Int = 0,
)
