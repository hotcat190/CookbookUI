package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.aigen.AIGenScreen
import com.example.androidcookbook.ui.features.aigen.AiScreenTheme
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedViewModel
import com.example.androidcookbook.ui.features.recipedetail.PostDetailsScreen
import com.example.androidcookbook.ui.features.userprofile.UserProfileScreen
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(navController: NavHostController, updateAppBar: () -> Unit) {
    navigation<Routes.App> (
        startDestination = Routes.App.Category
    ) {
        composable<Routes.App.Category> {
            updateAppBar()
            val categoryViewModel: CategoryViewModel = sharedViewModel(it, navController, Routes.App)
            CategoryScreen(categoryViewModel)
        }
        composable<Routes.App.AIChat> {
            updateAppBar()
            AiScreenTheme {
                AIGenScreen()
            }
        }
        composable<Routes.App.Newsfeed> {
            updateAppBar()

            val newsfeedViewModel = hiltViewModel<NewsfeedViewModel>()
            val posts = newsfeedViewModel.posts.collectAsState().value

            NewsfeedScreen(
                posts = posts,
                onSeeDetailsClick = {
                    navController.navigate(Routes.App.PostDetails)
                }
            )
        }
        composable<Routes.App.PostDetails> {
            updateAppBar()
            val postRoute = it.toRoute<Routes.App.PostDetails>()
            PostDetailsScreen(postRoute.id)

        }

        composable<Routes.App.UserProfile> {
            updateAppBar()
            val userRoute = it.toRoute<Routes.App.UserProfile>()
            UserProfileScreen(userRoute.id, {})
        }
    }
}