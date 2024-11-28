package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.example.androidcookbook.ui.common.dialog.MinimalDialog
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.features.auth.screens.ForgotPasswordScreen
import com.example.androidcookbook.ui.features.auth.screens.LoginScreen
import com.example.androidcookbook.ui.features.auth.screens.RegisterScreen
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.sharedViewModel

/**
 * Login, registration, forgot password screens nav graph builder
 * (Unauthenticated user)
 */
fun NavGraphBuilder.authScreens(navController: NavController, updateAppBar: () -> Unit, updateUser: (SignInResponse) -> Unit) {
    navigation<Routes.Auth>(
        startDestination = Routes.Auth.Login
    ) {
        // Scope the ViewModel to the navigation graph
        composable<Routes.Auth.Login> {
            updateAppBar()
            val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
            LoginScreen(
                onForgotPasswordClick = {
                    // TODO: navigate to ForgotPassword
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.Auth.Register)
                },
                onSignInClick = { username, password ->
                    authViewModel.signIn(username, password) { response ->
                        updateUser(response)
                    }
                    navController.navigate(Routes.DialogDestination)
                },
                onUseAsGuest = {
                    navController.navigate(Routes.App) {
                        popUpTo<Routes.Auth> { inclusive = true }
                    }
                }
            )
        }
        composable<Routes.Auth.Register> {
            updateAppBar()
            val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToSignIn = {
                    navController.navigate(Routes.Auth.Login)
                },
            )
        }
        composable<Routes.Auth.ForgotPassword> {
            updateAppBar()
            val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
            ForgotPasswordScreen(
                // TODO
            )
        }
        dialog<Routes.DialogDestination> {
            val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
            val authUiState by authViewModel.uiState.collectAsState()
            MinimalDialog(
                dialogMessage = authUiState.dialogMessage,
                onDismissRequest = {
                    authViewModel.changeOpenDialog(false)
                    authViewModel.changeDialogMessage("")

                    navController.popBackStack()

                    if (authUiState.signInSuccess) {
                        navController.navigate(Routes.App) {
                            // Clear authScreens from the backstack
                            popUpTo<Routes.Auth> {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    }
}

