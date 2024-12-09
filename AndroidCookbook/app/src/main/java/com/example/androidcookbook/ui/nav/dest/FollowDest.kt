package com.example.androidcookbook.ui.nav.dest

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.follow.BaseFollowListScreen
import com.example.androidcookbook.ui.features.follow.FollowListScreenType
import com.example.androidcookbook.ui.features.follow.FollowViewModel
import com.example.androidcookbook.ui.nav.CustomNavTypes
import com.example.androidcookbook.ui.nav.Routes
import kotlin.reflect.typeOf

fun NavGraphBuilder.follow(
    cookbookViewModel: CookbookViewModel,
    navController: NavHostController,
) {
    composable<Routes.Follow>(
        typeMap = mapOf(
            typeOf<User>() to CustomNavTypes.UserType
        )
    ) {
        val targetUser = it.toRoute<Routes.Follow>().user
        val startingScreen = it.toRoute<Routes.Follow>().type

        cookbookViewModel.updateCanNavigateBack(true)
        cookbookViewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        cookbookViewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)

        val followViewModel = hiltViewModel<FollowViewModel, FollowViewModel.FollowViewModelFactory> { factory ->
            factory.create(cookbookViewModel.user.value, targetUser)
        }

        var screenType by remember { mutableStateOf(startingScreen) }

        BaseFollowListScreen(
            user = targetUser,
            type = screenType,
            list = if (screenType == FollowListScreenType.Followers) {
                followViewModel.followers.collectAsState().value
            } else followViewModel.followers.collectAsState().value,
            onListItemClick = { user ->
                navController.navigate(Routes.OtherProfile(user))
            },
            isFollowing = { user ->
                followViewModel.checkFollowing(user)
            },
            onFollowButtonClick = { user ->
                followViewModel.followUser(user)
            },
            onBackButtonClick = {
                navController.popBackStack()
            },
            onFollowingNavigate = {
                screenType = FollowListScreenType.Following
            },
            onFollowersNavigate = {
                screenType = FollowListScreenType.Followers
            }
        )
    }
}