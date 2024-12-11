package com.example.androidcookbook.ui.nav.dest.profile

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.userprofile.edit.EditProfileScreen
import com.example.androidcookbook.ui.features.userprofile.edit.EditProfileViewModel
import com.example.androidcookbook.ui.nav.Routes

fun NavGraphBuilder.editProfile(
    viewModel: CookbookViewModel,
    navController: NavHostController,
) {
    composable<Routes.EditProfile>{
        viewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)
        viewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        viewModel.updateCanNavigateBack(true)

        val currentUser = viewModel.user.collectAsState().value

        val editProfileViewModel = hiltViewModel<EditProfileViewModel>()

        EditProfileScreen(
            user = currentUser,
            updateAvatarUri = { },
            updateBannerUri = { },
            onBioChange = { },
            onUpdate = {  },
            onBackButtonClick = {
                navController.navigateUp()
            }
        )
    }
}