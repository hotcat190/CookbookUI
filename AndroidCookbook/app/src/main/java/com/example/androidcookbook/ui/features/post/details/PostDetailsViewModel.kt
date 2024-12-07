package com.example.androidcookbook.ui.features.post.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.domain.model.post.Post
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDetailsViewModel.PostDetailsViewModelFactory::class)
class PostDetailsViewModel @AssistedInject constructor(
    private val postRepository: PostRepository,
    @Assisted private val _post: Post,
) : ViewModel() {

    @AssistedFactory
    interface PostDetailsViewModelFactory {
        fun create(post: Post): PostDetailsViewModel
    }

    var postUiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.Loading)
        private set

    var isLiked: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private set

    init {
        getPost()
        queryPostLike(_post.id)
    }

    private fun getPost() {
        viewModelScope.launch {
            val response = postRepository.getPost(_post.id)
            response.onSuccess {
                postUiState.update { PostUiState.Success(post = data) }
            }.onFailure {
                postUiState.update { PostUiState.Error(message = message()) }
            }
            Log.d("PostDetails", response.toString())
        }
    }

    private fun queryPostLike(postId: Int) {
        viewModelScope.launch {
            val response = postRepository.queryPostLike(postId)
            response.onSuccess {
                isLiked.update { true }
            }.onFailure {
                isLiked.update { false }
                Log.d("PostDetails", message())
            }
        }
    }

    private fun likePost() {
        viewModelScope.launch {
            val response = postRepository.likePost(_post.id)
            response.onSuccess {
                isLiked.update { true }
            }
        }
    }

    private fun unlikePost() {
        viewModelScope.launch {
            val response = postRepository.unlikePost(_post.id)
            response.onSuccess {
                isLiked.update { false }
            }
        }
    }

    fun toggleLike() {
        if (isLiked.value) {
            unlikePost()
        } else {
            likePost()
        }
        isLiked.update { !it }
    }


}

