package com.example.androidcookbook.ui.nav.utils

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.nav.Routes

/*
 * Extension function to tell the navController to navigate to the route only if the
 * currentDestination is not already on it.
 */
fun <T : Any> NavHostController.navigateIfNotOn(route: T) {
    if (currentDestination?.hasRoute(route) == true) {
        return
    }
    navigate(route)
}

fun <T: Any> NavDestination.hasRoute(route: T) =
    hasRoute(route::class)

/*
 * Extension function to check whether the current destination's hierarchy contains the parent route
 */
fun <T: Any> NavDestination.hasParent(route: T): Boolean =
    hierarchy.any {
        it.hasRoute(route::class)
    }

/*
 * Extension function to navigate to the other profile screen.
 */
fun NavHostController.navigateToProfile(currentUser: User, targetUser: User) {
    if (currentUser.id == targetUser.id) {
        navigateIfNotOn<Routes.App.UserProfile>(Routes.App.UserProfile(currentUser))
    } else {
        navigateIfNotOn<Routes.OtherProfile>(Routes.OtherProfile(targetUser))
    }
}
