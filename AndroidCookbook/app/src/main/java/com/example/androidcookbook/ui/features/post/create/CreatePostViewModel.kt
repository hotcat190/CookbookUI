package com.example.androidcookbook.ui.features.post.create

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.data.repositories.UploadRepository
import com.example.androidcookbook.domain.model.ingredient.Ingredient
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import com.example.androidcookbook.domain.usecase.createImageRequestBody
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val postRepository: PostRepository,
    private val uploadRepository: UploadRepository
) : ViewModel() {

    val postImageUri = MutableStateFlow<Uri?>(null)
    val postTitle = MutableStateFlow("")
    val postBody = MutableStateFlow("")
    val ingredients: MutableStateFlow<List<Ingredient>> = MutableStateFlow(listOf())
    val recipe: MutableStateFlow<List<String>> = MutableStateFlow(listOf())

    val isAddStepDialogOpen = MutableStateFlow(false)
    val isAddIngredientDialogOpen = MutableStateFlow(false)
    val updateStepDialogState = MutableStateFlow<UpdateStepDialogState>(UpdateStepDialogState.Closed)
    val updateIngredientDialogState = MutableStateFlow<UpdateIngredientDialogState>(UpdateIngredientDialogState.Closed)

    fun updatePostTitle(title: String) {
        postTitle.update { title }
    }

    fun updatePostBody(body: String) {
        postBody.update { body }
    }

    fun updatePostImageUri(uri: Uri?) {
        postImageUri.update { uri }
    }

    fun openAddStepDialog() {
        isAddStepDialogOpen.update { true }
    }

    fun openAddIngredientDialog() {
        isAddIngredientDialogOpen.update { true }
    }

    fun closeDialog() {
        isAddStepDialogOpen.update { false }
        isAddIngredientDialogOpen.update { false }
        updateStepDialogState.update { UpdateStepDialogState.Closed }
        updateIngredientDialogState.update { UpdateIngredientDialogState.Closed }
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.update {
            it + ingredient
        }
    }

    fun addStepToRecipe(step: String) {
        recipe.update {
            it + step
        }
    }

    fun deleteStep(index: Int) {
        recipe.update {
            it.filterIndexed { i, _ -> i != index }
        }
    }

    fun deleteIngredient(index: Int) {
        ingredients.update {
            it.filterIndexed { i, _ -> i != index }
        }
    }

    fun updateStep(index: Int, step: String) {
        recipe.update {
            val list = it.toMutableList()
            list[index] = step
            list
        }

    }

    fun openUpdateStep(index: Int) {
        updateStepDialogState.update { UpdateStepDialogState.Open(index, recipe.value[index]) }
    }

    fun updateIngredient(index: Int, ingredient: Ingredient) {
        ingredients.update {
            val list = it.toMutableList()
            list[index] = ingredient
            list
        }
    }

    fun openUpdateIngredient(index: Int) {
        updateIngredientDialogState.update { UpdateIngredientDialogState.Open(index, ingredients.value[index]) }
    }

    fun createPost(onSuccessNavigate: (Post) -> Unit) {
        viewModelScope.launch {
            val mainImage = uploadImage()
            val response = postRepository.createPost(
                PostCreateRequest(
                    title = postTitle.value,
                    description = postBody.value,
                    mainImage = mainImage,
                    cookTime = null,
                    ingredient = null,
                    steps = null,
                )
            )
            response.onSuccess {
                onSuccessNavigate(data.post)
            }.onFailure {
                //TODO
            }
        }
    }

    private suspend fun uploadImage(): String? {
        var mainImage: String? = null
        val imageRequestBody = postImageUri.value?.let { createImageRequestBody(context, it) }
        val imageResponse = imageRequestBody?.let { uploadRepository.uploadImage(it) }
        imageResponse?.onSuccess {
            mainImage = data.imageURL
        }?.onFailure {
            Log.e("CreatePostViewModel", message())
        }
        return mainImage
    }


}

sealed interface UpdateIngredientDialogState {
    data object Closed : UpdateIngredientDialogState
    data class Open(val index: Int, val ingredient: Ingredient) : UpdateIngredientDialogState
}

sealed interface UpdateStepDialogState {
    data object Closed : UpdateStepDialogState
    data class Open(val index: Int, val step: String) : UpdateStepDialogState
}
