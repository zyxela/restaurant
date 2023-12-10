package com.example.restaurant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val repository:MenuRepository):ViewModel(){

    val menu = MutableLiveData<List<Dish>>()
    val selected = MutableLiveData<IntArray>()
    val isCartEmpty = MutableLiveData<Boolean>()
    fun getMenu(){
        viewModelScope.launch {
          menu.value =  repository.getMenu()
            selected.value = menu.value?.let { IntArray(it.size){0} }
        }
    }

    fun addToCart(ids:List<Int>, userId:Int){
        viewModelScope.launch {
            repository.addToCart(ids, userId)
            checkCart(userId)
        }
    }

    fun checkCart(userId:Int){
        viewModelScope.launch {
            isCartEmpty.value = repository.checkCart(userId)
        }
    }
}