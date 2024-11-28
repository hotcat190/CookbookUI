package com.example.androidcookbook.ui.features.aigen

data class AIGenUiState(
    val mealTitle: String = "",
    val type: String = "",
    val portion: Int = 1,
    val cookingTime: Int = 1,
    val ingredient:MutableMap<String,String> = mutableMapOf(),
    val response: String = "",
    val isTakingInput: Boolean = true,
    val isProcessing: Boolean = false,
    val isDone: Boolean = false
)