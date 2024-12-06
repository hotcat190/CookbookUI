package com.example.androidcookbook.domain.model.ingredient

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val name: String,
    val quantity: String,
)
