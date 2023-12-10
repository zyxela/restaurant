package com.example.restaurant.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurant.viewModels.CartViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun Cart(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    val viewModel: CartViewModel = getViewModel()

    val cart by viewModel.cart.observeAsState(emptyList())
    viewModel.getCart(prefs.getInt("USER_ID", 3))

    val total by viewModel.total.observeAsState(0)
    viewModel.total(prefs.getInt("USER_ID", 3))

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Ваш заказ: ", fontSize = 54.sp, fontWeight = FontWeight(600))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                LazyColumn {
                    items(cart.size) { i ->
                        Text(text = "${cart[i].name}.....${cart[i].price}")
                    }
                }
                Text(text = "Итого..........${total}", fontWeight = FontWeight(500))
            }


        }
        Button(onClick = {
            viewModel.makeOrder(prefs.getInt("USER_ID", 3))
        }) {
            Text(text = "Забронировать столик")
        }
    }

}