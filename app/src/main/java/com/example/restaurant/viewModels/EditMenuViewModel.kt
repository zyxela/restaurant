package com.example.restaurant.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.EditMenuRepository
import kotlinx.coroutines.launch

class EditMenuViewModel(
    private val repository:EditMenuRepository
):ViewModel() {
    val menu = MutableLiveData<List<Dish>>()
    val dishes = MutableLiveData<List<Dish>>()

    fun removeDish(dish: Dish){
        viewModelScope.launch{
            repository.removeDishToMenu(dish)
            getMenu()
        }
    }
    fun addDishToMenu(dishId: Int){
        viewModelScope.launch{
            repository.addDishToMenu(dishId)
            getMenu()
        }
    }

    fun getDishes(){
        viewModelScope.launch {
            dishes.value =  repository.getDishes()
        }
    }
    fun editDish(dish:Dish){
        viewModelScope.launch {
            repository.editDish(dish)
            getMenu()
        }
    }

    fun getMenu(){
        viewModelScope.launch {
            menu.value = repository.getMenu()
        }
    }

}