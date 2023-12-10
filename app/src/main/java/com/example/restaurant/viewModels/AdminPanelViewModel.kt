package com.example.restaurant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.AdminPanelRepository
import kotlinx.coroutines.launch

class AdminPanelViewModel(private val repository: AdminPanelRepository) : ViewModel() {

    val orders = MutableLiveData<List<List<Dish>>>()
    val ordersIds = MutableLiveData<List<Int>>()
    fun getOrders() {
        viewModelScope.launch {
            orders.value = repository.getOrders()
            getOrdersIds()
        }
    }

    fun addDish(dish: String, description: String, price: Int, image: ByteArray) {
        viewModelScope.launch {
            repository.addDish(dish, description, price, image)
        }
    }

    fun acceptOrder(userId:Int, orderId:Int) {
        viewModelScope.launch{
            repository.acceptOrder(userId, orderId)
        }
    }

    fun getOrdersIds(){
        viewModelScope.launch {
            ordersIds.value = repository.getOrdersIds()
        }
    }


}