package com.example.restaurant.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.restaurant.entities.Dish
import com.example.restaurant.utils.toBitmap
import com.example.restaurant.viewModels.EditMenuViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun EditMenu() {
    val viewModel = getViewModel<EditMenuViewModel>()

    val menu by viewModel.menu.observeAsState(emptyList())
    viewModel.getMenu()


    Column(
        modifier = Modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        LazyRow(modifier = Modifier.padding(6.dp)) {
            items(menu.size) { i ->

                var name by remember {
                    mutableStateOf(menu[i].name)
                }

                var description by remember {
                    mutableStateOf(menu[i].description)
                }

                var price by remember {
                    mutableStateOf(menu[i].price.toString())
                }

                Card(
                    modifier = Modifier
                        .height(500.dp)
                        .width(300.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            modifier = Modifier
                                .height(200.dp)
                                .width(300.dp),
                            bitmap = menu[i].image.toBitmap().asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        TextField(
                            value = name,
                            onValueChange = {
                                name = it
                            })
                        TextField(
                            value = description,
                            onValueChange = {
                                description = it
                            })
                        TextField(
                            value = price,
                            onValueChange = {
                                price = it
                            })

                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            onClick = {
                                val d = Dish(
                                    menu[i].id,
                                    name,
                                    description,
                                    price.toInt(),
                                    menu[i].image
                                )
                                viewModel.editDish(
                                    d
                                )
                            }) {
                            Text(text = "Принять")
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            onClick = {
                                viewModel.removeDish(menu[i])
                            }) {
                            Text(text = "Убрать")
                        }

                    }
                }
            }
        }

        var state by remember {
            mutableStateOf(false)
        }
        if (state)
            AddToMenu(viewModel = viewModel, state = state)

        Button(modifier = Modifier.padding(6.dp), onClick = {
            state = !state
        }) {
            Text(text = "Добавить блюдо в меню")
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToMenu(viewModel: EditMenuViewModel, state: Boolean) {
    var show by remember {
        mutableStateOf(state)
    }

    val dishes by viewModel.dishes.observeAsState()
    viewModel.getDishes()

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var currentDish by remember {
        mutableStateOf("")
    }

    var currentId by remember {
        mutableIntStateOf(0)
    }

    if (show) {
        Dialog(onDismissRequest = { show = false }) {
            Card(modifier = Modifier.padding(24.dp)) {

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }) {

                    TextField(
                        value = currentDish,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        placeholder = {
                            Text(text = "Выберите блюдо")
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = {
                            isExpanded = false
                        }
                    ) {
                        dishes?.forEach { dish ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = dish.name)
                                },
                                onClick = {
                                    currentDish = dish.name
                                    currentId = dish.id
                                    isExpanded = false
                                }
                            )
                        }

                    }


                }

                Button(onClick = {
                    viewModel.addDishToMenu(currentId)
                }) {
                    Text(text = "Добавить")
                }
            }
        }
    }
}

