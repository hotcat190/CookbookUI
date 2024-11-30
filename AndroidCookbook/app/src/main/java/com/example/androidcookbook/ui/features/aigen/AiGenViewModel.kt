package com.example.androidcookbook.ui.features.aigen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AiGenViewModel : ViewModel() {

    private var _aiGenUiState = MutableStateFlow(AIGenUiState())

    val aiGenUiState: StateFlow<AIGenUiState> = _aiGenUiState.asStateFlow()

    fun updateMealTitle(updatedMealTitle: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                mealTitle = updatedMealTitle
            )
        }
    }

    fun updateType(updatedType: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                type = updatedType
            )
        }
    }

    fun updatePortion(updatedPortion: Int) {
        _aiGenUiState.update {
            currentState ->
            currentState.copy(
                portion = updatedPortion
            )
        }
    }

    fun updateCookingTime(updatedCookingTime: Int) {
        _aiGenUiState.update {
            currentState ->
            currentState.copy(
                cookingTime = updatedCookingTime
            )
        }
    }

    fun addingIngredient(ingredientName: String, portion: String) {
        _aiGenUiState.value.ingredient[ingredientName] = portion
    }

    fun removeIngredient(ingredientName: String) {
        _aiGenUiState.value.ingredient.remove(ingredientName)
    }

    fun updateIsProcessing() {
        _aiGenUiState.update {
            currentState ->
            currentState.copy(isProcessing = true, isTakingInput = true)
        }
    }

    fun updateIsDone() {
        _aiGenUiState.update {
            currentState ->
            currentState.copy(isDone = true, isProcessing = false, isTakingInput = false)
        }
    }

    fun updateIsTakingInput() {
        _aiGenUiState.update {
            currentState ->
            currentState.copy(isTakingInput = true, isDone = false, isProcessing = false)
        }
    }

}