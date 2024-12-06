package com.example.androidcookbook.ui.features.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.domain.model.post.Post
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

    var post: MutableStateFlow<Post> = MutableStateFlow(_post)
        private set

    init {
        getPost()
    }

    private fun getPost() {
        viewModelScope.launch {
            val response = postRepository.getPost(post.value.id)
            response.onSuccess {
                post.update { data }
            }.onFailure {
                //TODO
            }
            Log.d("PostDetails", response.toString())
        }
    }
}