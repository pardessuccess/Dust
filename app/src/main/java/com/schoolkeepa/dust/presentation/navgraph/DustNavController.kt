package com.schoolkeepa.dust.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object DustDestinations {
    const val SETTING_DESTINATION = "setting_destination"
    const val HOME_ROUTE = "home_route"
    const val INTRO_ROUTE = "intro_route"
    const val SURVEY_ROUTE = "survey_route"
    const val SEARCH_ROUTE = "search_route"
    const val MANUAL_ROUTE = "manual_route"

}

@Composable
fun rememberDustNavController(
    navController: NavHostController = rememberNavController(),
): DustNavController = remember(navController) {

    DustNavController(navController = navController)
}

class DustNavController(
    val navController: NavHostController
) {
    val currentRoute: String? = navController.currentDestination?.route

    fun navigateToRoute(route: String) {
        navController.navigate(route)
    }

    fun upPress() {
        navController.navigateUp()
    }
}