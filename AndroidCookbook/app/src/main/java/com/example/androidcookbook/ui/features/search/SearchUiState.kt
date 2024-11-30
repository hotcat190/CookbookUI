package com.example.androidcookbook.ui.features.search

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.domain.model.recipe.RecipeList
import com.example.androidcookbook.domain.model.user.User

enum class SearchScreenState {
    Food,
    Posts,
    Detail
}

data class SearchUiState(
    val searchQuery: String = "",
    val result: String = "",
    val resultList: List<Recipe> = listOf(),
    val fail: Boolean = false,
    val currentScreen: SearchScreenState = SearchScreenState.Food
)

object SamplePosts {
    val posts: List<Post> = listOf(
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
        ),
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
        ),
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
        ),
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
        ),
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
}