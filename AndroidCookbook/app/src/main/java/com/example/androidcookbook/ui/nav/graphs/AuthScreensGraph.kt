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
import com.example.androidcookbook.ui.features.auth.ForgotPasswordViewModel
import com.example.androidcookbook.ui.features.auth.components.SignTheme
import com.example.androidcookbook.ui.features.auth.screens.ForgotPasswordScreen
import com.example.androidcookbook.ui.features.auth.screens.LoginScreen
import com.example.androidcookbook.ui.features.auth.screens.RegisterScreen
import com.example.androidcookbook.ui.features.auth.screens.OtpCodeScreen
import com.example.androidcookbook.ui.features.auth.screens.ResetPasswordScreen
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
            SignTheme {
                updateAppBar()
                val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
                LoginScreen(
                    onForgotPasswordClick = {
                        navController.navigate(Routes.Auth.ForgotPassword)
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
        }
        composable<Routes.Auth.Register> {
            SignTheme {
                updateAppBar()
                val authViewModel: AuthViewModel = sharedViewModel(it, navController, Routes.Auth)
                RegisterScreen(
                    authViewModel = authViewModel,
                    onNavigateToSignIn = {
                        navController.navigate(Routes.Auth.Login)
                    },
                )
            }
        }
        navigation<Routes.Auth.ForgotPassword>(
            startDestination = Routes.Auth.ForgotPassword.Screen
        ) {
            composable<Routes.Auth.ForgotPassword.Screen> {
                SignTheme {
                    updateAppBar()

                    val forgotPasswordViewModel: ForgotPasswordViewModel =
                        sharedViewModel(it, navController, Routes.Auth.ForgotPassword)
                    val email = forgotPasswordViewModel.email.collectAsState().value

                    ForgotPasswordScreen(
                        email = email,
                        onEmailChanged = { newEmail ->
                            forgotPasswordViewModel.updateEmail(newEmail)
                        },
                        onSubmit = {
                            forgotPasswordViewModel.submitEmail()
                            //navController.navigate(Routes.Auth.ForgotPassword.Reset)
                        },
                        onNavigateToSignIn = {
                            navController.navigate(Routes.Auth.Login)
                        }
                    )
                    val openDialog = forgotPasswordViewModel.openDialog.collectAsState().value
                    val dialogMessage = forgotPasswordViewModel.dialogMessage.collectAsState().value
                    val success = forgotPasswordViewModel.successSubmit.collectAsState().value
                    if (openDialog) {
                        MinimalDialog(
                            dialogMessage = dialogMessage,
                            onDismissRequest = {
                                forgotPasswordViewModel.updateOpenDialog(false)
                                if (success) {
                                    navController.navigate(Routes.Auth.ForgotPassword.Otp)
                                    forgotPasswordViewModel.updateSuccessSubmit(false)
                                }
                            }
                        )
                    }
                }
            }
            composable<Routes.Auth.ForgotPassword.Otp> {
                SignTheme {
                    updateAppBar()

                    val forgotPasswordViewModel: ForgotPasswordViewModel =
                        sharedViewModel(it, navController, Routes.Auth.ForgotPassword)
                    val otpCode = forgotPasswordViewModel.otpCode.collectAsState().value

                    OtpCodeScreen(
                        otpCode = otpCode,
                        onOtpCodeChange = { newOtpCode ->
                            forgotPasswordViewModel.updateOtpCode(newOtpCode)
                        },
                        onSubmit = {
                            forgotPasswordViewModel.submitOtpRequest()
                            navController.navigate(Routes.Auth.ForgotPassword.Reset)
                        },
                        onNavigateToEmail = {
                            navController.navigate(Routes.Auth.ForgotPassword.Screen)
                        },
                        onNavigateToSignIn = {
                            navController.navigate(Routes.Auth.Login)
                        },
                        onGoBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }
            composable<Routes.Auth.ForgotPassword.Reset> {
                SignTheme {
                    updateAppBar()

                    val forgotPasswordViewModel: ForgotPasswordViewModel =
                        sharedViewModel(it, navController, Routes.Auth.ForgotPassword)
                    val password = forgotPasswordViewModel.password.collectAsState().value
                    val retypePassword =
                        forgotPasswordViewModel.retypePassword.collectAsState().value

                    ResetPasswordScreen(
                        password = password,
                        onPasswordChange = { newPassword ->
                            forgotPasswordViewModel.updatePassword(newPassword)
                        },
                        retypePassword = retypePassword,
                        onRetypePasswordChange = { newRetypePassword ->
                            forgotPasswordViewModel.updateRetypePassword(newRetypePassword)
                        },
                        onSubmit = {
                            forgotPasswordViewModel.submitPasswordResetRequest()
                        },
                        onNavigateToSignIn = {
                            navController.navigate(Routes.Auth.Login)
                        }
                    )

                    val openDialog = forgotPasswordViewModel.openDialog.collectAsState().value
                    val dialogMessage = forgotPasswordViewModel.dialogMessage.collectAsState().value
                    val success = forgotPasswordViewModel.successSubmit.collectAsState().value
                    if (openDialog) {
                        MinimalDialog(
                            dialogMessage = dialogMessage,
                            onDismissRequest = {
                                forgotPasswordViewModel.updateOpenDialog(false)
                                if (success) {
                                    navController.navigate(Routes.Auth.Login)
                                    forgotPasswordViewModel.updateSuccessSubmit(false)
                                }
                            }
                        )
                    }
                }
            }
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

