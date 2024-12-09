package com.example.androidcookbook.ui.nav.graphs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.nav.Routes

@Composable
fun AppEntryPoint(
    CookbookViewModel: CookbookViewModel = hiltViewModel<CookbookViewModel>(),
    navController: NavController
) {
    val isLoggedIn by CookbookViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Routes.App.Newsfeed)


        } else {
            navController.navigate(Routes.Auth.Login)

        }
    }
}
