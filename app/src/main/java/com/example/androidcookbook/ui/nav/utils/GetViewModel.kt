package com.example.androidcookbook.ui.nav.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.nav.Routes

@Composable
fun authViewModel(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
): AuthViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(Routes.Auth)
    }
    return hiltViewModel<AuthViewModel>(parentEntry)
}