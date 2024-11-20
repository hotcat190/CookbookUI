package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.androidcookbook.ui.features.aichat.AIChatScreen
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.post.CreatePostScreen
import com.example.androidcookbook.ui.features.search.SearchScreen
import com.example.androidcookbook.ui.features.search.SearchViewModel
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import kotlinx.serialization.Serializable

@Serializable object AppScreensGraph

@Serializable object Category
@Serializable object AIChat
@Serializable object Newsfeed
@Serializable object UserProfile
@Serializable object Search
@Serializable object CreatePost

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(navController: NavHostController) {
    navigation<AppScreensGraph> (
        startDestination = Category
    ) {
        composable<Category> {
            val categoryViewModel = hiltViewModel<CategoryViewModel>()
            val categoryUiState = categoryViewModel.categoryUiState.collectAsState().value
            CategoryScreen(categoryUiState = categoryUiState)
        }
        composable<Search> { entry ->
            val searchViewModel = hiltViewModel<SearchViewModel>(entry)
            val uiState = searchViewModel.uiState.value
            SearchScreen(
                result = uiState.searchQuery,
                onBackButtonClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<AIChat> {
            AIChatScreen()
        }
        composable<Newsfeed> {
            NewsfeedScreen()
        }
        composable<UserProfile> {
            UserProfileScreen()
        }
        composable<CreatePost> {
            CreatePostScreen(
                onPostButtonClick = {
                    //TODO: Connect to database
                },
                onBackButtonClick = {
                    navController.navigateUp()
                },
            )
        }
    }
}