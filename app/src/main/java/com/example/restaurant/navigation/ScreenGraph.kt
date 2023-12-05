package com.example.restaurant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurant.view.AdminPanel
import com.example.restaurant.view.Cart
import com.example.restaurant.view.Enter
import com.example.restaurant.view.Menu

@Composable
fun ScreenGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.EnterRoute.route) {

        composable(Route.EnterRoute.route) {
            Enter(navController)
        }

        composable(Route.MenuRoute.route){
            Menu(navController)
        }

        composable(Route.CartRoute.route){
            Cart(navController)
        }

        composable(Route.AdminPanelRoute.route){
            AdminPanel(navController)
        }
    }
}