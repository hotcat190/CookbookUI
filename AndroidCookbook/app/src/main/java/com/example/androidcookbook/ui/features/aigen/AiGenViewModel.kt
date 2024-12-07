package com.example.androidcookbook.ui.features.aigen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.network.AiGenService
import com.example.androidcookbook.data.repositories.AiGenRepository
import com.example.androidcookbook.domain.model.aigen.AiRecipe
import com.example.androidcookbook.domain.model.ingredient.Ingredient
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AiGenViewModel @Inject constructor(
    private val aiGenRepository: AiGenRepository
) : ViewModel() {

    private var _aiGenUiState = MutableStateFlow(AIGenUiState())

    val aiGenUiState: StateFlow<AIGenUiState> = _aiGenUiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> get() = _selectedImageUri


    private val _uploadResponse = MutableStateFlow<AiRecipe?>(null)
    val uploadResponse: StateFlow<AiRecipe?> = _uploadResponse.asStateFlow()


    fun updateSelectedUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    val gson = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()


    fun updateMealTitle(updatedMealTitle: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                mealTitle = updatedMealTitle
            )
        }
    }



    fun deleteIngredient(index: Int) {
        _aiGenUiState.update { currentState ->
            val updatedIngredient = currentState.ingredients.toMutableList()
            updatedIngredient.removeAt(index)
            currentState.copy(ingredients = updatedIngredient)
        }
    }


    fun updateIngredientName(index: Int, updatedIngredientName: String) {

        _aiGenUiState.update { currentState ->
            val updatedIngredient = currentState.ingredients.toMutableList()
            updatedIngredient[index] = updatedIngredient[index].copy(name = updatedIngredientName)
            currentState.copy(ingredients = updatedIngredient)
        }
    }




    fun updateIngredientQuantity(index: Int, updatedIngredientQuantity: String) {
        _aiGenUiState.update { currentState ->
            val updatedIngredient = currentState.ingredients.toMutableList()
            updatedIngredient[index] = updatedIngredient[index].copy(quantity = updatedIngredientQuantity)
            currentState.copy(ingredients = updatedIngredient)
        }
    }

    fun addEmptyIngredient() {
        _aiGenUiState.update { currentState ->
            val updatedIngredients = currentState.ingredients.toMutableList()
            updatedIngredients.add(Ingredient("",""))
            currentState.copy(ingredients = updatedIngredients)
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        _aiGenUiState.update { currentState ->
            val updatedIngredients = currentState.ingredients.toMutableList()
            updatedIngredients.add(ingredient)
            currentState.copy(ingredients = updatedIngredients)
        }
    }

    fun updatePortion(updatedPortion: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                portion = updatedPortion
            )
        }
    }

    fun updateCookingTime(updatedCookingTime: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                cookingTime = updatedCookingTime
            )
        }
    }

    fun updateNote(updatedNote: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                note = updatedNote
            )
        }
    }

    fun updateTimeMeasurement(updatedMeasurement: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                timeMeasurement = updatedMeasurement
            )
        }
    }

    fun updateServedAs(updatedServedAs: String) {
        _aiGenUiState.update { currentState ->
            currentState.copy(
                servedAs = updatedServedAs
            )
        }
    }

    fun updateIsProcessing() {
        _aiGenUiState.update { currentState ->
            currentState.copy(isProcessing = true, isTakingInput = false, isDone = false)
        }
    }

    fun isTakingInput(): Boolean {
        return _aiGenUiState.value.isTakingInput
    }

    fun isProcessing(): Boolean {
        return _aiGenUiState.value.isProcessing
    }

    fun isDone(): Boolean {
        return _aiGenUiState.value.isDone
    }

    fun updateIsDone() {
        _aiGenUiState.update { currentState ->
            currentState.copy(isDone = true, isProcessing = false, isTakingInput = false)
        }
    }

    fun updateIsTakingInput() {
        _aiGenUiState.update { currentState ->
            currentState.copy(isTakingInput = true, isDone = false, isProcessing = false)
        }
    }


    fun getUiStateJson(): String {


        // Assuming `aiGenUiState` is a StateFlow
        val currentState = runBlocking {
            _aiGenUiState.first() // Get the current value from the StateFlow
        }
        return gson.toJson(currentState) // Convert to JSON
    }

    suspend fun uploadImage(imagePart: MultipartBody.Part): AiRecipe? {
        return try {
            val response: Response<AiRecipe> = aiGenRepository.uploadImage(image = imagePart)
            if (response.isSuccessful) {
                _uploadResponse.value = response.body()
                response.body()
            } else {
                Log.d("Error", "Upload failed with code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.d("Error", "Upload Error: ${e.message}")
            null
        }
    }

}
