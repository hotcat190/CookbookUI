package com.example.androidcookbook.ui.nav.graphs

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
            NewsfeedScreen(
                // TODO
                posts = listOf(
                    Post(
                        id = 0,
                        author = User(),
                        title = "Shrimp salad cooking :)",
                        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
                        cookTime = null,
                        mainImage = null,
                        createdAt = "01/28/2024",
                        totalView = 0,
                        totalLike = 0,
                        totalComment = 0,
                        ingredient = null,
                        steps = null,
                    )
                ),
                {},
            )
        }
        composable<Routes.App.UserProfile> {
            updateAppBar()
            val user = it.toRoute<Routes.App.UserProfile>()
            UserProfileScreen(user.userId, {})
        }
    }
}