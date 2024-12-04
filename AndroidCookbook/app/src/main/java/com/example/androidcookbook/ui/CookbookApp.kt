package com.example.androidcookbook.ui

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidcookbook.ui.common.appbars.CookbookAppBarDefault
import com.example.androidcookbook.ui.common.appbars.CookbookBottomNavigationBar
import com.example.androidcookbook.ui.common.appbars.SearchBar
import com.example.androidcookbook.ui.features.post.CreatePostScreen
import com.example.androidcookbook.ui.features.post.CreatePostViewModel
import com.example.androidcookbook.ui.features.search.SearchScreen
import com.example.androidcookbook.ui.features.search.SearchViewModel
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.graphs.appScreens
import com.example.androidcookbook.ui.nav.graphs.authScreens
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: CookbookViewModel = hiltViewModel<CookbookViewModel>(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val uiState by viewModel.uiState.collectAsState()

    val currentUser by viewModel.user.collectAsState()
    Log.d("USERID", currentUser.toString())

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
                    },
                    onCreatePostClick = {
                        navController.navigateIfNotOn(Routes.CreatePost)
                    },
                    onMenuButtonClick = {
                        //TODO: Add menu button
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
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
                        navController.navigateIfNotOn(Routes.App.UserProfile(currentUser.id))
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
            }, updateUser = { response ->
                viewModel.updateUser(response)
            })
            appScreens(navController = navController, updateAppBar = {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.Default)
                viewModel.updateCanNavigateBack(false)
            })
            composable<Routes.Search> {
                val searchViewModel = hiltViewModel<SearchViewModel>()
                val searchUiState = searchViewModel.uiState.collectAsState().value

                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                viewModel.updateCanNavigateBack(true)
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Custom {
                    SearchBar(
                        onSearch = { searchViewModel.search(it) },
                        navigateBackAction = {
                            navController.navigateUp()
                        },
                    )
                })
                SearchScreen(
                    viewModel = searchViewModel,
                    searchUiState = searchUiState,
                    onBackButtonClick = {
                        navController.navigateUp()
                    }
                )
            }
            composable<Routes.CreatePost> {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                viewModel.updateCanNavigateBack(true)

//                val accessToken = viewModel.accessToken.collectAsState().value

//                val createPostViewModel = hiltViewModel<CreatePostViewModel, CreatePostViewModel.CreatePostViewModelFactory> { factory ->
//                    factory.create(accessToken)
//                }

                val createPostViewModel = hiltViewModel<CreatePostViewModel>()

                val postTitle by createPostViewModel.postTitle.collectAsState()
                val postBody by createPostViewModel.postBody.collectAsState()
                val postImageUri by createPostViewModel.postImageUri.collectAsState()

                CreatePostScreen(
                    author = currentUser,
                    postTitle = postTitle,
                    updatePostTitle = {
                        createPostViewModel.updatePostTitle(it)
                    },
                    postBody = postBody,
                    updatePostBody = {
                        createPostViewModel.updatePostBody(it)
                    },
                    postImageUri = postImageUri,
                    updatePostImageUri = {
                        createPostViewModel.updatePostImageUri(it)
                    },
                    onPostButtonClick = {
                        createPostViewModel.createPost()
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
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

