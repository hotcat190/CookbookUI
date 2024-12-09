package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.post.details.CommentBottomSheet
import com.example.androidcookbook.ui.features.post.details.PostDetailsScreen
import com.example.androidcookbook.ui.features.post.details.PostDetailsViewModel
import com.example.androidcookbook.ui.features.post.details.PostUiState
import com.example.androidcookbook.ui.nav.CustomNavTypes
import com.example.androidcookbook.ui.nav.Routes
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.postDetails(viewModel: CookbookViewModel) {
    composable<Routes.App.PostDetails>(
        typeMap = mapOf(
            typeOf<Post>() to CustomNavTypes.PostType,
        )
    ) {
        viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
        viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        viewModel.updateCanNavigateBack(true)

        val postRoute = it.toRoute<Routes.App.PostDetails>()

        val postDetailsViewModel =
            hiltViewModel<PostDetailsViewModel, PostDetailsViewModel.PostDetailsViewModelFactory> { factory ->
                factory.create(postRoute.post)
            }

        when (val postUiState = postDetailsViewModel.postUiState.collectAsState().value) {
            is PostUiState.Success -> {
                PostDetailsScreen(
                    post = postUiState.post,
                    isLiked = postDetailsViewModel.isLiked.collectAsState().value,
                    onLikedClick = {
                        postDetailsViewModel.toggleLike()
                    },
                    onCommentClick = {
                        postDetailsViewModel.updateShowBottomCommentSheet(true)
                    }
                )

                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                val scope = rememberCoroutineScope()

                if (postDetailsViewModel.showBottomCommentSheet.collectAsState().value) {
                    CommentBottomSheet(
                        comments = postDetailsViewModel.commentsFlow.collectAsState().value,
                        user = viewModel.user.collectAsState().value,
                        onSendComment = { content ->
                            postDetailsViewModel.sendComment(content)
                        },
                        onDismiss = {
                            postDetailsViewModel.updateShowBottomCommentSheet(false)
                        },
                        sheetState = sheetState,
                        modifier = Modifier
                    )
                }
            }

            is PostUiState.Error -> {
                // TODO
            }

            is PostUiState.Loading -> {
                // TODO
            }
        }
    }
}