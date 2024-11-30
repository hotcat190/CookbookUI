package com.example.androidcookbook.domain.model.aigen


import com.google.gson.annotations.Expose

data class Ingredient(
    @Expose val name: String = "",
    @Expose val quantity: String = ""
)

data class AiRecipe(
    val ingredients: List<Ingredient>,
    val recipes: List<String>
)