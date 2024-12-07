package com.example.androidcookbook.domain.model.aigen


import com.example.androidcookbook.domain.model.ingredient.Ingredient
import com.google.gson.annotations.Expose


data class AiRecipe(
    val ingredients: List<Ingredient>,
    val recipes: List<String>
)