package com.example.androidcookbook.ui.nav.utils

import androidx.navigation.NavHostController

/*
 * Extension function to tell the navController to navigate to the route only if the
 * currentDestination is not already on it.
 */
fun NavHostController.navigateIfNotOn(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}