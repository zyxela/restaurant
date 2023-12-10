package com.example.restaurant.view

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.restaurant.navigation.Route
import com.example.restaurant.viewModels.EnterViewModel
import org.koin.androidx.compose.getViewModel


internal enum class Auth {
    SignIn, SignUp
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Enter(navController: NavController) {
    val viewModel: EnterViewModel = getViewModel()

    val pageState = rememberPagerState {
        Auth.entries.size
    }


    Column(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 40.dp)) {

            HorizontalPager(
                state = pageState,
                key = {
                    Auth.entries[it]
                }
            ) { i ->

                Card(modifier = Modifier.padding(16.dp)) {
                    if (Auth.entries[i] == Auth.SignIn) {
                        SignIn(viewModel, navController)
                    } else {
                        SignUp(viewModel, navController)
                    }
                }


            }

        }

    }

}

@Composable
internal fun SignIn(viewModel: EnterViewModel, navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = "Войти",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal
            ),
            textAlign = TextAlign.Center
        )
        var loginText by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            value = loginText,
            onValueChange = { loginText = it },
            label = { Text(text = "Имя пользователя") },
            colors = TextFieldDefaults.colors(

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Пароль") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            placeholder = { Text("Пароль") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description =
                    if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        val enter by viewModel.enter.observeAsState(false)
        if (enter){
            if (prefs.getBoolean("USER_STATUS", false)){
                navController.navigate(Route.AdminPanelRoute.route)
            }else{
                navController.navigate(Route.MenuRoute.route)
            }
        }

        Button(onClick = {
            viewModel.auth(loginText, password, context)
        }) {
            Text(
                text = "Войти",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(3f),
                textAlign = TextAlign.Center
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUp(viewModel: EnterViewModel, navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = "Регистрация",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal
            ),
            textAlign = TextAlign.Center
        )
        var loginText by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            value = loginText,
            onValueChange = { loginText = it },
            label = { Text(text = "Имя пользователя") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent, //hide the indicator
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "пароль") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            placeholder = { Text("пароль") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        var passwordConfirm by rememberSaveable { mutableStateOf("") }


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            label = { Text(text = "повторите пароль") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            placeholder = { Text("text") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        val enter by viewModel.enter.observeAsState(false)
        if (enter){
            if (prefs.getBoolean("USER_STATUS", false)){
                navController.navigate(Route.AdminPanelRoute.route)
            }else{
                navController.navigate(Route.MenuRoute.route)
            }
        }


        Button(
            enabled = password == passwordConfirm && password != "" && loginText != "",
            onClick = {
                viewModel.regist(loginText, password, context)
            }) {
            Text(
                text = "Зарегистрироваться",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(3f),
                textAlign = TextAlign.Center
            )
        }


    }
}