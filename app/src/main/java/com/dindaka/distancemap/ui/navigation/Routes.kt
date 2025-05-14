package com.dindaka.distancemap.ui.navigation

sealed class Routes(val route: String) {
    data object Map : Routes("map")
    data object List : Routes("list/{markers}")
}