package com.example.androidcookbook.ui.nav.dest.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.nav.Routes

fun NavGraphBuilder.editProfile(
    viewModel: CookbookViewModel,
    currentUser: User,
    navController: NavHostController,
) {
    composable<Routes.EditProfile>{
        viewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)
        viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        viewModel.updateCanNavigateBack(true)


    }
}