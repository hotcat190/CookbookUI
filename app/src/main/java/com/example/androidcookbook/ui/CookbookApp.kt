package com.example.androidcookbook.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidcookbook.ui.common.appbars.CookbookAppBar
import com.example.androidcookbook.ui.common.appbars.CookbookBottomNavigationBar
import com.example.androidcookbook.ui.common.appbars.SearchBar
import com.example.androidcookbook.ui.nav.graphs.AIChat
import com.example.androidcookbook.ui.nav.graphs.AuthScreensGraph
import com.example.androidcookbook.ui.nav.graphs.Category
import com.example.androidcookbook.ui.nav.graphs.CreatePost
import com.example.androidcookbook.ui.nav.graphs.Newsfeed
import com.example.androidcookbook.ui.nav.graphs.Search
import com.example.androidcookbook.ui.nav.graphs.UserProfile
import com.example.androidcookbook.ui.nav.graphs.appScreens
import com.example.androidcookbook.ui.nav.graphs.authScreens
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val viewModel: CookbookViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!uiState.showTopBar) {
                return@Scaffold
            }
            if (currentDestination?.hasRoute(Search::class) == true) {
                SearchBar(
                    onValueChange = {_->},
                    navigateBackAction = {},
                    searchQuery = "",
                )
            } else {
                CookbookAppBar(
                    showBackButton = uiState.canNavigateBack,
                    searchButtonAction = {
                        navController.navigate(Search)
                    },
                    onCreatePostClick = {
                        navController.navigateIfNotOn(CreatePost)
                        viewModel.updateCanNavigateBack(true)
                    },
                    onMenuButtonClick = {
                        //TODO: Add menu button
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
                        viewModel.updateCanNavigateBack(false)
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            if (!uiState.showTopBar) {
                return@Scaffold
            }
            CookbookBottomNavigationBar(
                onHomeClick = {
                    navController.navigateIfNotOn(Category)
                },
                onChatClick = {
                    navController.navigateIfNotOn(AIChat)
                },
                onNewsfeedClick = {
                    navController.navigateIfNotOn(Newsfeed)
                },
                onUserProfileClick = {
                    navController.navigateIfNotOn(UserProfile)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AuthScreensGraph,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            authScreens(navController = navController)
            appScreens(navController = navController)
        }
    }
}

@Preview
@Composable
fun CookbookAppPreview() {
    CookbookApp()
}