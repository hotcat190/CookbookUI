package com.example.androidcookbook.ui.nav.dest

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedViewModel
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.navigateToProfile
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

fun NavGraphBuilder.newsfeed(
    cookbookViewModel: CookbookViewModel,
    navController: NavHostController,
) {
    composable<Routes.App.Newsfeed> {
        cookbookViewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
        cookbookViewModel.updateBottomBarState(CookbookUiState.BottomBarState.Default)
        cookbookViewModel.updateCanNavigateBack(false)

        val newsfeedViewModel = sharedViewModel<NewsfeedViewModel>(it, navController, Routes.App)
        val posts = newsfeedViewModel.posts.collectAsState().value

        RefreshableScreen(
            onRefresh = { newsfeedViewModel.refresh() }
        ) {
            NewsfeedScreen(
                posts = posts,
                currentUser = cookbookViewModel.user.collectAsState().value,
                onEditPost = { post ->
                    navController.navigate(Routes.UpdatePost(post))
                },
                onDeletePost = { post ->
                    newsfeedViewModel.deletePost(post)
                },
                onSeeDetailsClick = { post ->
                    navController.navigate(Routes.App.PostDetails(post))
                },
                onUserClick = { user ->
                    if (user.id == cookbookViewModel.user.value.id) {
                        navController.navigate(Routes.App.UserProfile(user))
                    } else {
                        navController.navigate(Routes.OtherProfile(user))
                    }
                    cookbookViewModel.updateCanNavigateBack(true)
                },
                onLoadMore = {
                    newsfeedViewModel.loadMore()
                }
            )
        }
    }
}