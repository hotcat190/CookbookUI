package com.example.androidcookbook.ui.nav.dest.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.features.follow.FollowListScreenType
import com.example.androidcookbook.ui.features.follow.FollowViewModel
import com.example.androidcookbook.ui.features.userprofile.GuestProfile
import com.example.androidcookbook.ui.features.userprofile.UserPostState
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import com.example.androidcookbook.ui.features.userprofile.UserProfileUiState
import com.example.androidcookbook.ui.features.userprofile.UserProfileViewModel
import com.example.androidcookbook.ui.features.follow.FollowButton
import com.example.androidcookbook.ui.features.follow.FollowButtonState
import com.example.androidcookbook.ui.features.userprofile.userPostPortion
import com.example.androidcookbook.ui.nav.CustomNavTypes
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn
import com.example.androidcookbook.ui.nav.utils.navigateToProfile
import com.example.androidcookbook.ui.nav.utils.sharedViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.otherProfile(
    cookbookViewModel: CookbookViewModel,
    currentUser: User,
    navController: NavHostController,
) {
    composable<Routes.OtherProfile>(
        typeMap = mapOf(
            typeOf<User>() to CustomNavTypes.UserType
        )
    ) {
        val user = it.toRoute<Routes.OtherProfile>().user

        cookbookViewModel.updateCanNavigateBack(true)
        cookbookViewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
        cookbookViewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)

        Log.d("UserProfileViewModelOwner", "LocalViewModelStoreOwner: ${LocalViewModelStoreOwner.current}")

        val userProfileViewModel =
            hiltViewModel<UserProfileViewModel, UserProfileViewModel.UserProfileViewModelFactory>(

            ) { factory ->
                factory.create(user)
            }

        val followViewModel = sharedViewModel<FollowViewModel, FollowViewModel.FollowViewModelFactory>(
            it, navController, Routes.OtherProfile(user)
        ) { factory ->
            factory.create(
                currentUser,
                user,
            )
        }

        val userProfileUiState = userProfileViewModel.uiState.collectAsState().value

        RefreshableScreen(
            onRefresh = {
                userProfileViewModel.refresh()
                followViewModel.refresh()
            }
        ) {
            when (userProfileUiState) {
                is UserProfileUiState.Loading -> {
                    Text(
                        "Loading"
                    )
                }

                is UserProfileUiState.Success -> {
                    val userPostState = userProfileViewModel.userPostState.collectAsState().value

                    Log.d("UserProfileViewModelOwner", "LocalViewModelStoreOwner: ${LocalViewModelStoreOwner.current}")


                    val isFollowing = followViewModel.isFollowing.collectAsState().value
                    UserProfileScreen(
                        user = userProfileUiState.user,
                        headerButton = {
                            FollowButton(
                                onFollowButtonClick = {
                                    followViewModel.toggleFollow(userProfileUiState.user)
                                },
                                followButtonState = (
                                    if (isFollowing) FollowButtonState.Following
                                    else FollowButtonState.Follow
                                )
                            )
                        },
                        followersCount = followViewModel.followers.collectAsState().value.size,
                        followingCount = followViewModel.following.collectAsState().value.size,
                        onFollowersClick = {
                            navController.navigateIfNotOn(
                                Routes.Follow(userProfileUiState.user, FollowListScreenType.Followers)
                            )
                        },
                        onFollowingClick = {
                            navController.navigateIfNotOn(
                                Routes.Follow(userProfileUiState.user, FollowListScreenType.Following)
                            )
                        },
                    ) {
                        when (userPostState) {
                            is UserPostState.Loading -> item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text("Loading user posts")
                                }
                            }

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
                                        navController.navigateToProfile(currentUser, user)
                                    },
                                    currentUser = currentUser
                                )
                            }

                            is UserPostState.Failure -> item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text("Failed to fetch user posts.")
                                }
                            }

                            is UserPostState.Guest -> item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text("Login to see your posts.")
                                }
                            }
                        }
                    }
                }

                is UserProfileUiState.Failure -> {
                    GuestProfile("Failed to fetch user profile.")
                }

                is UserProfileUiState.Guest -> {
                    GuestProfile("Login to see your posts.")
                }
            }
        }
    }
}