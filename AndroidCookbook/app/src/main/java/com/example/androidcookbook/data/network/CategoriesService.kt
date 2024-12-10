package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.aigen.AiRecipe
import com.example.androidcookbook.domain.model.category.CategoryResponse
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.domain.model.recipe.RecipeList
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoriesService {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("random.php")
    suspend fun getRandomMeal(): RecipeList

    @GET("filter.php")
    suspend fun getMealByCategories(@Query("c") category: String): RecipeList
}