package com.example.androidcookbook.ui.features.post.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.domain.model.post.Comment
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.post.SendCommentRequest
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.domain.usecase.DeletePostUseCase
import com.example.androidcookbook.domain.usecase.MakeToastUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDetailsViewModel.PostDetailsViewModelFactory::class)
class PostDetailsViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val _post: Post,
    @Assisted private val currentUser: User,
    private val postRepository: PostRepository,
    private val deletePostUseCase: DeletePostUseCase,
    private val makeToastUseCase: MakeToastUseCase,
) : ViewModel() {

    @AssistedFactory
    interface PostDetailsViewModelFactory {
        fun create(
            post: Post,
            currentUser: User,
        ): PostDetailsViewModel
    }

    var postUiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.Loading)
        private set
    var showBottomCommentSheet: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private set
    var commentsFlow: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
        private set
    var commentPage: MutableStateFlow<Int> = MutableStateFlow(1)
        private set
    var editCommentState: MutableStateFlow<EditCommentState> = MutableStateFlow(EditCommentState.NotEditing)
        private set
    var isPostLiked: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private set
    var isPostBookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private set

    init {
        refresh()
    }

    fun refresh() {
        getPost()
        //        postUiState.update { PostUiState.Success(post = _post) }
        queryPostLike(_post.id)
        queryPostBookmark(_post.id)
        getComments(false)
    }

    fun updateShowBottomCommentSheet(value: Boolean) {
        showBottomCommentSheet.update { value }
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
            val response = postRepository.queryPostLike(postId, currentUser.id)
            response.onSuccess {
                isPostLiked.update { data.isLiked }
            }.onFailure {
                isPostLiked.update { false }
                Log.d("PostDetails", message())
            }
        }
    }

    private fun queryPostBookmark(postId: Int) {
        viewModelScope.launch {
            val response = postRepository.queryPostBookmark(postId, currentUser.id)
            response.onSuccess {
                isPostBookmarked.update { data.isFavorited }
            }.onFailure {
                isPostBookmarked.update { false }
                Log.d("PostDetails", message())
            }
        }
    }

    private fun likePost() {
        viewModelScope.launch {
            val response = postRepository.likePost(_post.id)
            response.onSuccess {
                isPostLiked.update { true }
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to like post")
            }
        }
    }

    private fun unlikePost() {
        viewModelScope.launch {
            val response = postRepository.unlikePost(_post.id)
            response.onSuccess {
                isPostLiked.update { false }
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to unlike post")
            }
        }
    }

    fun togglePostLike() {
        if (isPostLiked.value) {
            unlikePost()
        } else {
            likePost()
        }
    }

    private fun bookmarkPost() {
        viewModelScope.launch {
            val response = postRepository.bookmarkPost(_post.id)
            response.onSuccess {
                isPostBookmarked.update { true }
            }.onFailure {
                showToast("Failed to bookmark post")
            }
        }
    }

    private fun unBookmarkPost() {
        viewModelScope.launch {
            val response = postRepository.unBookmarkPost(_post.id)
            response.onSuccess {
                isPostBookmarked.update { false }
            }.onFailure {
                showToast("Failed to unbookmark post")
            }
        }
    }

    fun togglePostBookmark() {
        if (isPostBookmarked.value) {
            unBookmarkPost()
        } else {
            bookmarkPost()
        }
    }

    private fun getComments(reset: Boolean) {
        if (reset) {
            commentsFlow.update { emptyList() }
            commentPage.update { 1 }
        }
        viewModelScope.launch {
            val response = postRepository.getComments(_post.id, currentUser.id, commentPage.value)
            response.onSuccess {
                commentsFlow.update { it + data.comments }
                commentPage.update { it + 1 }
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to get comments")
            }
        }
    }

    fun sendComment(content: String) {
        viewModelScope.launch {
            val response = postRepository.sendComment(
                postId = _post.id,
                request = SendCommentRequest(content),
            )
            response.onSuccess {
                getComments(true)
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to send comment")
            }
        }
    }

    fun editComment(content: String) {
        viewModelScope.launch {
            val comment = (editCommentState.value as EditCommentState.Editing).comment
            val response = postRepository.editComment(
                commentId = comment.id, SendCommentRequest(content)
            )
            response.onSuccess {
                getComments(true)
                exitEditCommentState()
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to edit comment")
            }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            val response = postRepository.deleteComment(
                commentId = comment.id,
            )
            response.onSuccess {
                getComments(true)
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to delete comment")
            }
        }
    }

    fun toggleLikeComment(comment: Comment) {
        viewModelScope.launch {
            if (comment.isLiked) {
                unlikeComment(comment)
            } else {
                likeComment(comment)
            }
        }
    }

    private fun likeComment(comment: Comment) {
        viewModelScope.launch {
            val response = postRepository.likeComment(comment.id)
            response.onSuccess {
                // update the target comment isLiked field
                updateCommentIsLiked(comment, true)
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to like comment")
            }
        }
    }

    private fun unlikeComment(comment: Comment) {
        viewModelScope.launch {
            val response = postRepository.unlikeComment(comment.id)
            response.onSuccess {
                updateCommentIsLiked(comment, false)
                Log.d("PostDetails", data.toString())
            }.onFailure {
                Log.d("PostDetails", message())
                showToast("Failed to unlike comment")
            }
        }
    }

    private fun updateCommentIsLiked(comment: Comment, isLiked: Boolean) {
        commentsFlow.update { comments ->
            comments.map {
                if (it.id == comment.id) {
                    it.copy(
                        isLiked = isLiked,
                        totalLike = if (isLiked) it.totalLike + 1 else it.totalLike - 1,
                    )
                } else {
                    it
                }
            }
        }
    }

    fun enterEditCommentState(comment: Comment) {
        viewModelScope.launch {
            editCommentState.update { EditCommentState.Editing(comment) }
        }
    }

    fun exitEditCommentState() {
        viewModelScope.launch {
            editCommentState.update { EditCommentState.NotEditing }
        }
    }

    private fun showToast(message: String) {
        viewModelScope.launch {
            makeToastUseCase(message)
        }
    }

    fun deletePost(onSuccessNavigate: () -> Unit) {
        viewModelScope.launch {
            val response = deletePostUseCase(_post)
            response.onSuccess {
                onSuccessNavigate()
            }.onFailure {
                viewModelScope.launch {
                    makeToastUseCase("Failed to delete post")
                }
            }
        }
    }
}


