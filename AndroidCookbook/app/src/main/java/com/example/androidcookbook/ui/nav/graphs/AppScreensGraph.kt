package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.usecase.DeletePostUseCase
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.features.aigen.AIGenScreen
import com.example.androidcookbook.ui.features.aigen.AiScreenTheme
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedViewModel
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(navController: NavHostController, updateAppBar: () -> Unit, cookbookViewModel: CookbookViewModel,) {
    navigation<Routes.App> (
        startDestination = Routes.App.Newsfeed
    ) {
        composable<Routes.App.Category> {
            updateAppBar()
            val categoryViewModel: CategoryViewModel = sharedViewModel(it, navController, Routes.App)
            CategoryScreen(categoryViewModel)
        }
        composable<Routes.App.AIChef> {
            updateAppBar()
            AiScreenTheme {
                AIGenScreen()
            }
        }
        newsfeed(updateAppBar, navController, cookbookViewModel)

        userProfile(updateAppBar, cookbookViewModel, navController)
    }
}

private fun NavGraphBuilder.newsfeed(
    updateAppBar: () -> Unit,
    navController: NavHostController,
    cookbookViewModel: CookbookViewModel,
) {
    composable<Routes.App.Newsfeed> {
        updateAppBar()

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
                }
            )
        }
    }
}

private fun NavGraphBuilder.userProfile(
    updateAppBar: () -> Unit,
    cookbookViewModel: CookbookViewModel,
    navController: NavHostController,
) {
    composable<Routes.App.UserProfile> {
        updateAppBar()
        val userRoute = it.toRoute<Routes.App.UserProfile>()
        UserProfileScreen(
            userId = userRoute.id,
            currentUser = cookbookViewModel.user.collectAsState().value,
            onEditPost = { post ->
                navController.navigate(Routes.UpdatePost(post))
            },
            onPostSeeDetailsClick = { post ->
                navController.navigate(Routes.App.PostDetails(post))
            }
        )
    }
}