package com.example.restaurant.repository

import com.example.restaurant.entities.Dish

interface EditMenuRepository {
    suspend fun addDishToMenu(dish: Int)
    suspend fun editDish(dish: Dish)
    suspend fun removeDishToMenu(dish: Dish)
    suspend fun getMenu():List<Dish>?
    suspend fun getDishes():List<Dish>?


}