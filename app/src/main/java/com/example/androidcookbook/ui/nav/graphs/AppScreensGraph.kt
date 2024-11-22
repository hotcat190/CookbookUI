package com.example.androidcookbook.ui.nav.graphs

import android.util.Log
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
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

/**
 * App screens nav graph builder
 */
fun NavGraphBuilder.appScreens(navController: NavHostController, updateAppBar: () -> Unit) {
    navigation<Routes.App> (
        startDestination = Routes.App.Category
    ) {
        Log.d("Navigation", "Route.App")
        composable<Routes.App.Category> {
            Log.d("Navigation", "Route.App.Category")
            updateAppBar()
            val categoryViewModel: CategoryViewModel = sharedViewModel(it, navController, Routes.App)
            CategoryScreen(categoryViewModel)
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