package com.example.restaurant.repository

import com.example.restaurant.entities.Dish

interface MenuRepository {
    suspend fun getMenu():List<Dish>?

    suspend fun addToCart(ids:List<Int>, userId:Int)
    suspend fun checkCart(userId:Int):Boolean
}