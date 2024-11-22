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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidcookbook.ui.common.appbars.CookbookAppBarDefault
import com.example.androidcookbook.ui.common.appbars.CookbookBottomNavigationBar
import com.example.androidcookbook.ui.features.post.CreatePostScreen
import com.example.androidcookbook.ui.features.search.SearchScreen
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.graphs.appScreens
import com.example.androidcookbook.ui.nav.graphs.authScreens
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: CookbookViewModel = viewModel(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (uiState.topBarState) {
                is CookbookUiState.TopBarState.NoTopBar -> {}
                is CookbookUiState.TopBarState.Custom -> (uiState.topBarState as CookbookUiState.TopBarState.Custom).topAppBar.invoke()
                is CookbookUiState.TopBarState.Default -> CookbookAppBarDefault(
                    showBackButton = uiState.canNavigateBack,
                    searchButtonAction = {
                        navController.navigateIfNotOn(Routes.Search)
                        viewModel.updateCanNavigateBack(true)
                    },
                    onCreatePostClick = {
                        navController.navigateIfNotOn(Routes.CreatePost)
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
            when (uiState.bottomBarState) {
                is CookbookUiState.BottomBarState.NoBottomBar -> {}
                is CookbookUiState.BottomBarState.Default -> CookbookBottomNavigationBar(
                    onHomeClick = {
                        navController.navigateIfNotOn(Routes.App.Category)
                    },
                    onChatClick = {
                        navController.navigateIfNotOn(Routes.App.AIChat)
                    },
                    onNewsfeedClick = {
                        navController.navigateIfNotOn(Routes.App.Newsfeed)
                    },
                    onUserProfileClick = {
                        navController.navigateIfNotOn(Routes.App.UserProfile(0)) // TODO: UserId logic
                    },
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Auth,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            authScreens(navController = navController, updateAppBar = {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
            })
            appScreens(navController = navController, updateAppBar = {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.Default)
            })
            composable<Routes.Search> { entry ->
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)

                SearchScreen(
                    providesCustomAppBar = {
                        viewModel.updateTopBarState(CookbookUiState.TopBarState.Custom(it))
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
                        viewModel.updateCanNavigateBack(false)
                    }
                )
            }
            composable<Routes.CreatePost> {
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                CreatePostScreen(
                    onPostButtonClick = {
                        //TODO: Connect to database
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
                        viewModel.updateCanNavigateBack(false)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun CookbookAppPreview() {
    CookbookApp()
}

