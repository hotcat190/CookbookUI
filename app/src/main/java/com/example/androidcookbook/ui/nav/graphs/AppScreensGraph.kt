package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.androidcookbook.ui.features.aichat.AIChatScreen
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import com.example.androidcookbook.ui.nav.Routes

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(navController: NavHostController, updateAppBar: () -> Unit) {
    navigation<Routes.App> (
        startDestination = Routes.App.Category
    ) {
        composable<Routes.App.Category> {
            updateAppBar()
            val categoryViewModel = hiltViewModel<CategoryViewModel>()
            val categoryUiState = categoryViewModel.categoryUiState.collectAsState().value
            CategoryScreen(categoryUiState = categoryUiState)
        }
        composable<Routes.App.AIChat> {
            updateAppBar()
            AIChatScreen()
        }
        composable<Routes.App.Newsfeed> {
            updateAppBar()
            NewsfeedScreen()
        }
        composable<Routes.App.UserProfile> {
            updateAppBar()
            val user = it.toRoute<Routes.App.UserProfile>()
            UserProfileScreen(user.userId)
        }
    }
}