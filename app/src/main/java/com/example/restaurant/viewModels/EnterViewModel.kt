package com.example.restaurant.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.Database
import com.example.restaurant.repository.EnterRepository
import kotlinx.coroutines.launch

class EnterViewModel(private val repository:EnterRepository):ViewModel() {

    val enter = MutableLiveData<Boolean>(false)

    fun auth(login: String, password: String, context: Context) {
        viewModelScope.launch {
            repository.auth(login, password, context)
        }
    }

    fun regist(login: String, password: String, context: Context) {
        viewModelScope.launch {
            enter.value = repository.regist(login, password, context)
        }
    }
}