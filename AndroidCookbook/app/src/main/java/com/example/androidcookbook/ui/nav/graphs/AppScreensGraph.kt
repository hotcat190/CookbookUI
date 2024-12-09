package com.example.androidcookbook.ui.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.aigen.AIGenScreen
import com.example.androidcookbook.ui.features.aigen.AiScreenTheme
import com.example.androidcookbook.ui.features.category.CategoryScreen
import com.example.androidcookbook.ui.features.category.CategoryViewModel
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.dest.newsfeed
import com.example.androidcookbook.ui.nav.dest.profile.userProfile
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

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

