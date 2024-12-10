package com.example.androidcookbook.ui.features.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.CategoriesRepository
import com.example.androidcookbook.domain.model.category.Category
import com.example.androidcookbook.domain.model.recipe.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private var _categoryUiState: MutableStateFlow<CategoryUiState> = MutableStateFlow(CategoryUiState.Loading)
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState



    init {

        getCategoriesAndRandomMeals()
    }

    private fun getCategoriesAndRandomMeals() {
        viewModelScope.launch {
            _categoryUiState.update { CategoryUiState.Loading }
            try {
                val recipeList: MutableList<Recipe> = mutableListOf()
                val categoryList: MutableList<Category> =  categoriesRepository.getCategories()

                repeat(8) {
                    recipeList.add(categoriesRepository.getRandomMeal().meals.first())
                }

                for(category in categoryList) {
                    category.strCategoryThumb = categoriesRepository.getMealByCategories(category.strCategory).meals.first().strMealThumb
                }





                _categoryUiState.update{ CategoryUiState.Success(categoryList,recipeList) }
            } catch (e: IOException) {
                _categoryUiState.update{ CategoryUiState.Error }
            } catch (e: HttpException) {
                _categoryUiState.update{ CategoryUiState.Error }
            }
        }
    }



    fun refresh() {
        getCategoriesAndRandomMeals()
    }
}