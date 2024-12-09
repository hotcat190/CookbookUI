package com.example.androidcookbook.ui

import android.app.Activity
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.ui.common.appbars.AppBarTheme
import com.example.androidcookbook.ui.common.appbars.CookbookAppBarDefault
import com.example.androidcookbook.ui.common.appbars.CookbookBottomNavigationBar
import com.example.androidcookbook.ui.common.appbars.SearchBar
import com.example.androidcookbook.ui.features.auth.theme.SignLayoutTheme
import com.example.androidcookbook.ui.features.post.create.AddIngredientDialog
import com.example.androidcookbook.ui.features.post.create.AddStepDialog
import com.example.androidcookbook.ui.features.post.create.CreatePostScreen
import com.example.androidcookbook.ui.features.post.create.CreatePostViewModel
import com.example.androidcookbook.ui.features.post.create.UpdateIngredientDialog
import com.example.androidcookbook.ui.features.post.create.UpdateIngredientDialogState
import com.example.androidcookbook.ui.features.post.create.UpdateStepDialog
import com.example.androidcookbook.ui.features.post.create.UpdateStepDialogState
import com.example.androidcookbook.ui.features.post.details.PostDetailsScreen
import com.example.androidcookbook.ui.features.post.details.PostDetailsViewModel
import com.example.androidcookbook.ui.features.post.details.PostUiState
import com.example.androidcookbook.ui.features.search.SearchScreen
import com.example.androidcookbook.ui.features.search.SearchViewModel
import com.example.androidcookbook.ui.nav.CustomNavTypes
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.graphs.AppEntryPoint
import com.example.androidcookbook.ui.nav.graphs.appScreens
import com.example.androidcookbook.ui.nav.graphs.authScreens
import com.example.androidcookbook.ui.nav.dest.createPost
import com.example.androidcookbook.ui.nav.dest.postDetails
import com.example.androidcookbook.ui.nav.dest.updatePost
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: CookbookViewModel = hiltViewModel<CookbookViewModel>(),
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    val uiState = viewModel.uiState.collectAsState().value

    val currentUser = viewModel.user.collectAsState().value
    Log.d("USERID", currentUser.toString())

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (uiState.topBarState) {
                is CookbookUiState.TopBarState.Auth -> {
                    SignLayoutTheme {
                        updateSystemBarColors(
                            MaterialTheme.colorScheme.onBackground.toArgb(),
                            MaterialTheme.colorScheme.surfaceContainerLowest.toArgb(),
                            true
                        )
                    }
                }

                is CookbookUiState.TopBarState.Custom -> (uiState.topBarState as CookbookUiState.TopBarState.Custom).topAppBar.invoke()
                is CookbookUiState.TopBarState.Default -> {
                    AppBarTheme {
                        updateSystemBarColors(
                            Color.TRANSPARENT,
                            MaterialTheme.colorScheme.background.toArgb()
                        )
                        CookbookAppBarDefault(
                            showBackButton = uiState.canNavigateBack,
                            searchButtonAction = {
                                navController.navigateIfNotOn(Routes.Search)
                            },
                            onCreatePostClick = {
                                navController.navigateIfNotOn(Routes.CreatePost)
                            },
                            onMenuButtonClick = {

                            },
                            onBackButtonClick = {
                                navController.navigateUp()
                            },
                            scrollBehavior = scrollBehavior,
                            onLogoutClick = {
                                viewModel.logout()
                                navController.navigateIfNotOn(Routes.Auth)
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            when (uiState.bottomBarState) {
                is CookbookUiState.BottomBarState.NoBottomBar -> {}
                is CookbookUiState.BottomBarState.Default -> CookbookBottomNavigationBar(
                    onCategoryClick = {
                        navController.navigateIfNotOn(Routes.App.Category)
                    },
                    onAiChatClick = {
                        navController.navigateIfNotOn(Routes.App.AIChef)
                    },
                    onNewsfeedClick = {
                        navController.navigateIfNotOn(Routes.App.Newsfeed)
                    },
                    onUserProfileClick = {
                        navController.navigateIfNotOn(Routes.App.UserProfile(currentUser))
                    },
                    onCreatePostClick = {
                        navController.navigateIfNotOn(Routes.CreatePost)
                    },
                    currentUser = currentUser,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "check_auth",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            composable("check_auth") {
                AppEntryPoint(navController = navController)
            }



            authScreens(navController = navController, updateAppBar = {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Auth)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
            }, updateUser = { response,username,password ->
                viewModel.updateUser(response,username,password)
            })
            appScreens(navController = navController, updateAppBar = {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.Default)
                viewModel.updateCanNavigateBack(false)
            }, cookbookViewModel = viewModel)
            composable<Routes.Search> {
                val searchViewModel = hiltViewModel<SearchViewModel>()
                val searchUiState = searchViewModel.uiState.collectAsState().value

                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                viewModel.updateCanNavigateBack(true)
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Custom {
                    AppBarTheme {
                        SearchBar(
                            onSearch = { searchViewModel.search(it) },
                            navigateBackAction = {
                                navController.navigateUp()
                            },
                        )
                    }
                })
                SearchScreen(
                    viewModel = searchViewModel,
                    searchUiState = searchUiState,
                    onBackButtonClick = {
                        navController.navigateUp()
                    }
                )
            }


            createPost(viewModel, currentUser, navController)
            updatePost(viewModel, currentUser, navController)

            postDetails(viewModel, navController)

        }
    }
}

@Composable
private fun updateSystemBarColors(
    statusBarColor: Int,
    navigationBarColor: Int,
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }
}

@Preview
@Composable
fun CookbookAppPreview() {
    CookbookApp()
}

