package com.example.androidcookbook.data.mocks

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User

val FakePostsList = listOf(
    Post(
        id = 0,
        author = User(),
        title = "Shrimp salad cooking :)",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
        cookTime = null,
        mainImage = null,
        createdAt = "01/28/2024",
        totalView = 0,
        totalLike = 0,
        totalComment = 0,
        ingredient = null,
        steps = null,
    )
)