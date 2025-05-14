package com.dindaka.distancemap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dindaka.distancemap.data.remote.dto.LatLngDto
import com.dindaka.distancemap.ui.screens.list.ListScreen
import com.dindaka.distancemap.ui.screens.map.MapScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Map.route) {
        composable(Routes.Map.route) { MapScreen(navController) }
        composable(Routes.List.route) {
            val service = navController.previousBackStackEntry?.savedStateHandle?.get<List<LatLngDto>>("markers")
            if(service != null) {
                ListScreen(navController, service)
            }
        }
    }
}
