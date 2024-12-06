package com.example.androidcookbook.ui.features.search

import com.example.androidcookbook.domain.model.recipe.Recipe

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