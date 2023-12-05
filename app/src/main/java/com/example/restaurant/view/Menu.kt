package com.example.restaurant.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurant.viewModels.MenuViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun Menu(navController:NavController){

    val viewModel:MenuViewModel = getViewModel()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "MyCart")
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(6.dp)) {
        LazyRow{
            items(1){item->

            }
        }
    }
}