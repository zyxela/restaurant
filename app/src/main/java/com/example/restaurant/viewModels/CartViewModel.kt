package com.example.restaurant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    val cart = MutableLiveData<List<Dish>>()
    val total = MutableLiveData<Int>()
    fun getCart(userId: Int) {
        viewModelScope.launch {
            cart.value = repository.getCart(userId)
        }
    }

    fun makeOrder(userId: Int) {
        viewModelScope.launch {
            repository.placeAnOrder(userId)
        }
    }

    fun total(userId:Int){
        viewModelScope.launch {
            total.value = repository.getTotal(userId)
        }
    }

}