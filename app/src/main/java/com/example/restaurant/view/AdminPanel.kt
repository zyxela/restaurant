package com.example.restaurant.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restaurant.R
import com.example.restaurant.navigation.Route
import com.example.restaurant.viewModels.AdminPanelViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AdminPanel(navController: NavController) {

    val viewModel: AdminPanelViewModel = getViewModel()

    var addDish by remember {
        mutableStateOf(false)
    }

    var cc by remember {
        mutableStateOf(false)
    }
    if (cc) {
        CurrentOrders(viewModel = viewModel)
    }

    if (addDish) {
        AddDish(viewModel)
    }

    var editMenu by remember {
        mutableStateOf(false)
    }
    if (editMenu) {

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            addDish = !addDish
        }) {
            Text(text = "Добавить блюдо")
        }

        Button(onClick = {
            navController.navigate(Route.EditMenuRoute.route)
        }) {
            Text(text = "Редактировать меню")
        }


        Button(onClick = {
            cc = !cc
        }) {
            Text(text = "Просмотреть заказы")
        }
    }

}

@Composable
fun CurrentOrders(viewModel: AdminPanelViewModel) {

    val order by viewModel.orders.observeAsState(emptyList())
    viewModel.getOrders()

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var dialog by remember {
        mutableStateOf(true)
    }
    if (dialog) {
        Dialog(onDismissRequest = {
            dialog = false
        }) {
            Card {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(order.size) { i ->
                        var ord  = 0
                        Column(modifier = Modifier.padding(4.dp)) {
                            LazyColumn {
                                items(order[i].size) { ordr ->
                                    ord = order[i][ordr].id
                                    Text(text = order[i][ordr].name)
                                }
                            }

                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                onClick = {
                                    viewModel.acceptOrder(prefs.getInt("USER_ID", 3), ord)
                                }) {
                                Text(text = "Принять")
                            }
                        }
                    }


                }


            }

        }


    }

}


@Composable
fun AddDish(viewModel: AdminPanelViewModel) {
    val context = LocalContext.current


    var name by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                uri = it
            })

    var dialog by remember {
        mutableStateOf(true)
    }
    if (dialog) {
        Dialog(onDismissRequest = {
            dialog = false
        }) {
            Card(modifier = Modifier.padding(4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {

                    AsyncImage(
                        modifier = Modifier
                            .clickable {
                                singlePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .size(250.dp, 200.dp),
                        model = uri,
                        placeholder = painterResource(id = R.drawable.image),
                        error = painterResource(id = R.drawable.image),
                        contentDescription = "",
                        // contentScale = ContentScale.Crop
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text(text = "Название блюда")
                        })

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        label = {
                            Text(text = "Описание блюда")
                        })

                    OutlinedTextField(
                        value = price,
                        onValueChange = {
                            price = it
                        },
                        label = {
                            Text(text = "Цена")
                        })

                    Button(onClick = {
                        val inputStream = context.contentResolver.openInputStream(uri!!)
                        val bytes = inputStream?.readBytes()!!
                        viewModel.addDish(name, description, price.toInt(), bytes)
                        inputStream.close()
                    }) {
                        Text(text = "Добавить блюдо")
                    }


                }
            }
        }
    }
}