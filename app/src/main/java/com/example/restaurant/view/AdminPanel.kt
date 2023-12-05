package com.example.restaurant.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.restaurant.viewModels.AdminPanelViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AdminPanel(navController: NavController){

    val viewModel:AdminPanelViewModel = getViewModel()


}