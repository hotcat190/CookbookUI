package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.androidcookbook.ui.nav.NavigationRoutes
import com.example.androidcookbook.ui.nav.utils.authViewModel
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.features.auth.screens.ForgotPasswordScreen
import com.example.androidcookbook.ui.features.auth.screens.LoginScreen
import com.example.androidcookbook.ui.features.auth.screens.RegisterScreen

/**
 * Login, registration, forgot password screens nav graph builder
 * (Unauthenticated user)
 */
fun NavGraphBuilder.authScreens(navController: NavController) {
    navigation(
        route = NavigationRoutes.AuthScreens.NavigationRoute.route,
        startDestination = NavigationRoutes.AuthScreens.Login.route
    ) {
        // Scope the ViewModel to the navigation graph
        composable(route = NavigationRoutes.AuthScreens.Login.route) { backStackEntry ->
            val authViewModel = authViewModel(backStackEntry, navController)
            val authUiState by authViewModel.uiState.collectAsState()
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(NavigationRoutes.AuthScreens.Register.route)
                },
                onForgotPasswordClick = {},
                onSignInClick = { username, password ->
                    authViewModel.signIn(username, password)
                },
                isDialogOpen = authUiState.openDialog,
                dialogMessage = authUiState.dialogMessage,
                onDialogDismiss = {
                    authViewModel.changeOpenDialog(false)
                }
            )
            // Navigate to AppScreens graph when sign in is successful.
            LaunchedEffect(authUiState.signInSuccess) {
                if (authViewModel.uiState.value.signInSuccess) {
                    navController.navigate(NavigationRoutes.AppScreens.NavigationRoute.route) {
                        // Clear authScreens from the backstack
                        popUpTo(NavigationRoutes.AuthScreens.NavigationRoute.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
        composable(route = NavigationRoutes.AuthScreens.Register.route) { backStackEntry ->
            val authViewModel: AuthViewModel = authViewModel(backStackEntry, navController)
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToSignIn = {
                    navController.navigate(NavigationRoutes.AuthScreens.Login.route)
                }
            )
        }
        composable(route = NavigationRoutes.AuthScreens.ForgotPassword.route) { backStackEntry ->
            val authViewModel: AuthViewModel = authViewModel(backStackEntry, navController)
            ForgotPasswordScreen(
                // TODO
            )
        }
    }
}

