package com.example.androidcookbook.ui.features.category

import com.example.androidcookbook.domain.model.category.Category
import com.example.androidcookbook.domain.model.recipe.Recipe

sealed interface CategoryUiState{
    data class Success(val categories: List<Category>, val randomMeals: List<Recipe>) : CategoryUiState
    data object Error : CategoryUiState
    data object Loading : CategoryUiState
}
