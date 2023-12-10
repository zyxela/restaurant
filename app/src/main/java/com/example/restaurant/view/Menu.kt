package com.example.restaurant.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurant.entities.Dish
import com.example.restaurant.navigation.Route
import com.example.restaurant.utils.toBitmap
import com.example.restaurant.viewModels.MenuViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun Menu(navController: NavController) {

    val viewModel: MenuViewModel = getViewModel()

    val menu by viewModel.menu.observeAsState(emptyList())
    viewModel.getMenu()

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    val showRedDot by viewModel.isCartEmpty.observeAsState(false)
    viewModel.checkCart(prefs.getInt("USER_ID", 3))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Box{
                Icon(
                    modifier = Modifier.clickable {
                        navController.navigate(Route.CartRoute.route)
                    },
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "MyCart"
                )
                if (showRedDot) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Red, shape = CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = -8.dp)
                    ) {
                        // Пустой блок для отображения красной точки
                    }
                }
            }

        }
        val selected by viewModel.selected.observeAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .height(650.dp)
        ) {


            items(menu.size) { i ->
                var s by remember {
                    mutableStateOf(false)
                }
                MenuItem(menu[i], s) {
                    s = !s
                    if (s)
                        selected?.set(i, 1)
                    else
                        selected?.set(i, 0)

                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            onClick = {
                val ids = mutableListOf<Int>()
                for (i in selected?.indices ?: 0..0) {
                    if (selected?.get(i)  == 1) {
                        ids.add(menu[i].id)
                    }
                }

                viewModel.addToCart(ids.toList(), prefs.getInt("USER_ID", 3))
            }) {
            Text("Добавить в корзину")
        }
    }
}

@Composable
internal fun MenuItem(dish: Dish, selected: Boolean, onChecked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(2.dp)
                ) {
                    Image(
                        modifier = Modifier.size(60.dp),
                        bitmap = dish.image.toBitmap().asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }

                Column {
                    Text(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(500),
                        text = " Название: ${dish.name}"
                    )
                    Text(" Описание: ${dish.description}")
                    Text(" Цена: ${dish.price}")
                }
            }

            CircleCheckbox(selected = selected) {
                onChecked()
            }
        }
    }
}

@Composable
fun CircleCheckbox(selected: Boolean, enabled: Boolean = true, onChecked: () -> Unit) {

    val color = MaterialTheme.colorScheme
    val imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if (selected) color.primary.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (selected) Color.White else Color.Transparent

    IconButton(
        onClick = { onChecked() },
        modifier = Modifier.offset(x = 4.dp, y = 4.dp),
        enabled = enabled
    ) {

        Icon(
            imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox"
        )
    }
}