package com.example.androidcookbook.ui.nav.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.example.androidcookbook.ui.common.dialog.MinimalDialog
import com.example.androidcookbook.ui.features.auth.AuthUiState
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.features.auth.login.LoginScreen
import com.example.androidcookbook.ui.features.auth.screens.ForgotPasswordScreen
import com.example.androidcookbook.ui.features.auth.screens.RegisterScreen
import com.example.androidcookbook.ui.nav.utils.authViewModel
import kotlinx.serialization.Serializable

@Serializable object AuthScreensGraph

@Serializable object Login
@Serializable object Register
@Serializable object ForgotPassword
@Serializable object MinimalDialog

/**
 * Login, registration, forgot password screens nav graph builder
 * (Unauthenticated user)
 */
fun NavGraphBuilder.authScreens(navController: NavController) {
    navigation<AuthScreensGraph>(
        startDestination = Login
    ) {
        // Scope the ViewModel to the navigation graph
        composable<Login> {
            val authViewModel: AuthViewModel = authViewModel(it, navController)
            val loginState: AuthUiState by authViewModel.uiState.collectAsState()
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Register)
                },
                onForgotPasswordClick = {},
                onSignInClick = { username, password ->
                    authViewModel.signIn(username, password) { navController.navigate(MinimalDialog) }
                },
            )
            // Navigate to AppScreens graph when sign in is successful.
            LaunchedEffect(loginState.signInSuccess) {
                if (loginState.signInSuccess) {
                    navController.navigate(Category) {
                        // Clear authScreens from the backstack
                        popUpTo<AuthScreensGraph> {
                            inclusive = true
                        }
                    }
                }
            }
        }
        composable<Register> { backStackEntry ->
            val authViewModel: AuthViewModel = authViewModel(backStackEntry, navController)
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToSignIn = {
                    navController.navigate(Login)
                },

            )
        }
        composable<ForgotPassword> { backStackEntry ->
            val authViewModel: AuthViewModel = authViewModel(backStackEntry, navController)
            ForgotPasswordScreen(
                // TODO
            )
        }
        dialog<MinimalDialog> { backStackEntry ->
            val authViewModel: AuthViewModel = authViewModel(backStackEntry, navController)
            val authUiState by authViewModel.uiState.collectAsState()
            MinimalDialog(
                dialogMessage = authUiState.dialogMessage,
                onDismissRequest = {
                    navController.popBackStack()
                }
            )
        }
    }
}

