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
import androidx.compose.runtime.getValue
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
import com.example.androidcookbook.ui.nav.graphs.appScreens
import com.example.androidcookbook.ui.nav.graphs.authScreens
import com.example.androidcookbook.ui.nav.utils.navigateIfNotOn
import kotlin.reflect.typeOf

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
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (uiState.topBarState) {
                is CookbookUiState.TopBarState.Auth -> {
                    SignLayoutTheme {
                        updateSystemBarColors(
                            MaterialTheme.colorScheme.onBackground.toArgb(),
                            MaterialTheme.colorScheme.background.toArgb(),
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
                                //TODO: Add menu button
                            },
                            onBackButtonClick = {
                                navController.navigateUp()
                            },
                            scrollBehavior = scrollBehavior
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
                        navController.navigateIfNotOn(Routes.App.UserProfile(currentUser.id))
                    },
                    onCreatePostClick = {
                        navController.navigateIfNotOn(Routes.CreatePost)
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
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Auth)
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
            composable<Routes.CreatePost> {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                viewModel.updateCanNavigateBack(true)

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
                    recipe = createPostViewModel.recipe.collectAsState().value,
                    onAddNewStep = {
                        createPostViewModel.openAddStepDialog()
                    },
                    updateStep = {
                        createPostViewModel.openUpdateStep(it)
                    },
                    deleteStep = {
                        createPostViewModel.deleteStep(it)
                    },
                    ingredients = createPostViewModel.ingredients.collectAsState().value,
                    onAddNewIngredient = {
                        createPostViewModel.openAddIngredientDialog()
                    },
                    updateIngredient = {
                        createPostViewModel.openUpdateIngredient(it)
                    },
                    deleteIngredient = {
                        createPostViewModel.deleteIngredient(it)
                    },
                    postImageUri = postImageUri,
                    updatePostImageUri = {
                        createPostViewModel.updatePostImageUri(it)
                    },
                    onPostButtonClick = {
                        createPostViewModel.createPost(
                            onSuccessNavigate = { post ->
                                navController.navigate(Routes.App.PostDetails(post))
                            }
                        )
                    },
                    onBackButtonClick = {
                        navController.navigateUp()
                    },
                )
                if (createPostViewModel.isAddStepDialogOpen.collectAsState().value) {
                    AddStepDialog(
                        onAddStep = {
                            createPostViewModel.addStepToRecipe(it)
                            createPostViewModel.closeDialog()
                        },
                        onDismissRequest = {
                            createPostViewModel.closeDialog()
                        }
                    )
                }
                if (createPostViewModel.isAddIngredientDialogOpen.collectAsState().value) {
                    AddIngredientDialog(
                        onAddIngredient = {
                            createPostViewModel.addIngredient(it)
                            createPostViewModel.closeDialog()
                        },
                        onDismissRequest = {
                            createPostViewModel.closeDialog()
                        }
                    )
                }
                when (val updateStepDialogState = createPostViewModel.updateStepDialogState.collectAsState().value) {
                    is UpdateStepDialogState.Open -> {
                        UpdateStepDialog(
                            step = updateStepDialogState.step,
                            onUpdateStep = {
                                createPostViewModel.updateStep(updateStepDialogState.index, it)
                                createPostViewModel.closeDialog()
                            },
                            onDismissRequest = {
                                createPostViewModel.closeDialog()
                            }
                        )
                    }
                    UpdateStepDialogState.Closed -> {}
                }
                when (val updateIngredientDialogState = createPostViewModel.updateIngredientDialogState.collectAsState().value) {
                    is UpdateIngredientDialogState.Open -> {
                        UpdateIngredientDialog(
                            ingredient = updateIngredientDialogState.ingredient,
                            onUpdateIngredient = {
                                createPostViewModel.updateIngredient(updateIngredientDialogState.index, it)
                                createPostViewModel.closeDialog()
                            },
                            onDismissRequest = {
                                createPostViewModel.closeDialog()
                            }
                        )
                    }
                    UpdateIngredientDialogState.Closed -> {}
                }
            }

            composable<Routes.App.PostDetails>(
                typeMap = mapOf(
                    typeOf<Post>() to CustomNavTypes.PostType,
                )
            ) {
                viewModel.updateTopBarState(CookbookUiState.TopBarState.Default)
                viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
                viewModel.updateCanNavigateBack(true)

                val postRoute = it.toRoute<Routes.App.PostDetails>()

                val postDetailsViewModel = hiltViewModel<PostDetailsViewModel, PostDetailsViewModel.PostDetailsViewModelFactory> { factory ->
                    factory.create(postRoute.post)
                }

                when (val postUiState = postDetailsViewModel.postUiState.collectAsState().value) {
                    is PostUiState.Success -> {
                        PostDetailsScreen(
                            post = postUiState.post,
                            isLiked = postDetailsViewModel.isLiked.collectAsState().value,
                            onLikedClick = {
                                postDetailsViewModel.toggleLike()
                            }
                        )
                    }
                    is PostUiState.Error -> {
                        // TODO
                    }
                    is PostUiState.Loading -> {
                        // TODO
                    }
                }
            }
        }
    }
}

@Composable
private fun updateSystemBarColors(statusBarColor: Int, navigationBarColor: Int, darkTheme: Boolean = isSystemInDarkTheme()) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }
}

@Preview
@Composable
fun CookbookAppPreview() {
    CookbookApp()
}

