package com.example.androidcookbook.ui.features.search

import androidx.compose.foundation.pager.PagerState
import com.example.androidcookbook.data.mocks.SamplePosts
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.recipe.Recipe

enum class SearchScreenState {
    Food,
    Posts,
    Detail,
    Waiting
}

enum class SearchTab {
    Posts,
    Recommended
}

data class SearchUiState(
    val searchQuery: String = "",
    val result: String = "",
    val resultList: List<Recipe> = listOf(),
    val fail: Boolean = false,
    val currentScreen: SearchScreenState = SearchScreenState.Waiting,
    val currentPost: Post = SamplePosts.posts[0],
)