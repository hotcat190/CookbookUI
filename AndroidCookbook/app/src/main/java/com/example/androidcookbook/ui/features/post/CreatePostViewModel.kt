package com.example.androidcookbook.ui.features.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CreatePostViewModel.CreatePostViewModelFactory::class)
class CreatePostViewModel @AssistedInject constructor(
    private val postRepository: PostRepository,
    @Assisted private val accessToken: String,
) : ViewModel() {

    @AssistedFactory
    interface CreatePostViewModelFactory {
        fun create(accessToken: String): CreatePostViewModel
    }

    val postImageUri = MutableStateFlow<Uri?>(null)
    var postTitle = MutableStateFlow("")
    var postBody = MutableStateFlow("")

    fun updatePostTitle(title: String) {
        postTitle.update { title }
    }

    fun updatePostBody(body: String) {
        postBody.update { body }
    }

    fun updatePostImageUri(uri: Uri?) {
        postImageUri.update { uri }
    }

    fun createPost() {
        viewModelScope.launch {
//            val mainImage = imageRepository.uploadImage(postImage.value)

            try {
                postRepository.createPost(
                    PostCreateRequest(
                        title = postTitle.value,
                        description = postBody.value,
                        mainImage = null, // TODO upload mainImage
                        cookTime = null,
                        ingredient = null,
                        steps = null,
                    )
                )
            } catch (e: Exception) {
                Log.e("CreatePost", e.stackTraceToString())
            }
        }
    }
}