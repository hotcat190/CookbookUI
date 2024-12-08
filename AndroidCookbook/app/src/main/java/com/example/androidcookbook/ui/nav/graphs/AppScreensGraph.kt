package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.features.aigen.AIGenScreen
import com.example.androidcookbook.ui.features.aigen.AiScreenTheme
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedCard
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedViewModel
import com.example.androidcookbook.ui.features.userprofile.GuestProfile
import com.example.androidcookbook.ui.features.userprofile.UserInfo
import com.example.androidcookbook.ui.features.userprofile.UserPostState
import com.example.androidcookbook.ui.features.userprofile.UserProfileHeader
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import com.example.androidcookbook.ui.features.userprofile.UserProfileUiState
import com.example.androidcookbook.ui.features.userprofile.UserProfileViewModel
import com.example.androidcookbook.ui.features.userprofile.userPostPortion
import com.example.androidcookbook.ui.nav.CustomNavTypes
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn
import com.example.androidcookbook.ui.nav.utils.sharedViewModel
import kotlin.reflect.typeOf

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(
    navController: NavHostController,
    updateAppBar: () -> Unit,
    cookbookViewModel: CookbookViewModel,
) {
    navigation<Routes.App>(
        startDestination = Routes.App.Newsfeed
    ) {
        composable<Routes.App.Category> {
            updateAppBar()
            val categoryViewModel: CategoryViewModel =
                sharedViewModel(it, navController, Routes.App)
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
                },
                onUserClick = { user ->
                    navController.navigate(Routes.App.UserProfile(user))
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
    composable<Routes.App.UserProfile>(
        typeMap = mapOf(
            typeOf<User>() to CustomNavTypes.UserType
        )
    ) {
        updateAppBar()
        val user = it.toRoute<Routes.App.UserProfile>().user

        val userProfileViewModel = hiltViewModel<UserProfileViewModel, UserProfileViewModel.UserProfileViewModelFactory        >(

        ) { factory ->
            factory.create(user)
        }

        val userProfileUiState = userProfileViewModel.uiState.collectAsState().value

        RefreshableScreen(
            onRefresh = { userProfileViewModel.refresh() }
        ) {
            when (userProfileUiState) {
                is UserProfileUiState.Loading -> {
                    Text(
                        "Loading"
                    )
                }
                is UserProfileUiState.Success -> {
                    val userPostState = userProfileViewModel.userPostState.collectAsState().value
                    UserProfileScreen(
                        user = userProfileUiState.user,
                    ) {
                        when (userPostState) {
                            is UserPostState.Loading -> item { Text("Loading user posts") }
                            is UserPostState.Success -> {
                                userPostPortion(
                                    userPosts = userPostState.userPosts,
                                    onEditPost = { post ->
                                        navController.navigate(Routes.UpdatePost(post))
                                    },
                                    onDeletePost = { post ->
                                        userProfileViewModel.deletePost(post)
                                    },
                                    onPostSeeDetailsClick = { post ->
                                        navController.navigate(Routes.App.PostDetails(post))
                                    },
                                    onUserClick = { user ->
                                        navController.navigateIfNotOn(Routes.App.UserProfile(user))
                                    },
                                    user = userProfileUiState.user
                                )
                            }

                            is UserPostState.Failure -> item { Text("Failed to fetch user posts.") }
                            UserPostState.Guest -> item {Text("Login to view your posts.")}
                        }
                    }
                }
                UserProfileUiState.Failure -> {
                    // TODO: FAILURE SCREEN - CURRENTLY IS GUEST SCREEN
                    GuestProfile()
                }
            }
        }
    }
}

