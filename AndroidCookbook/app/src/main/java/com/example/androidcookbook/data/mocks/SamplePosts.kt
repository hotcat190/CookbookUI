package com.example.androidcookbook.data.mocks

import com.example.androidcookbook.domain.model.ingredient.Ingredient
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.GUEST_ID
import com.example.androidcookbook.domain.model.user.User

object SampleUser {
    val users = buildList<User> {
        repeat(10) {
            add (
                User(
                    id = GUEST_ID + it,
                    name = "Ly Duc",
                    avatar = null,
                    bio = if (it % 2 == 0) "I like suffering I like suffering I like sufferingI like sufferingI like sufferingI like sufferingI like suffering" else "I like suffering\nand eating and drinking and playing video games",
                    totalFollowers = 0,
                    totalFollowing = 1
                )
            )
        }
    }
}

object SamplePosts {
    val posts: List<Post> = buildList {
        repeat(10) {
            add(
                Post(
                    id = it,
                    author = User(
                        id = 1,
                        name = "Ly Duc",
                        avatar = null,
                        bio = "I like suffering",
                        totalFollowers = 0,
                        totalFollowing = 0
                    ),
                    title = "Shrimp salad cooking :)",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
                    cookTime = "",
                    mainImage = null,
                    createdAt = "2024-01-28T00:00:00.000Z",
                    totalView = 0,
                    totalLike = 0,
                    totalComment = 0,
                    ingredient = listOf(
                        Ingredient("Ingredient1", "Quantity 1")
                    ),
                    steps = listOf(
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
                    )
                )
            )
        }
    }
}