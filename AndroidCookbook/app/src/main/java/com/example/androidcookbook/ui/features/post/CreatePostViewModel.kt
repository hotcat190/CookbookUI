package com.example.androidcookbook.ui.features.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.post.PostCreateRequest
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

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

    fun createPost(onSuccessNavigate: (Post) -> Unit) {
        viewModelScope.launch {
//            val mainImage = imageRepository.uploadImage(postImage.value)

            try {
                val response = postRepository.createPost(
                    PostCreateRequest(
                        title = postTitle.value,
                        description = postBody.value,
                        mainImage = null, // TODO upload mainImage
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
            } catch (e: Exception) {
                Log.e("CreatePost", e.stackTraceToString())
            }
        }
    }
}