package com.example.restaurant.repository

import com.example.restaurant.entities.Dish

interface CartRepository {
    suspend fun getCart(userId:Int):List<Dish>?

    suspend fun placeAnOrder(userId:Int)

    suspend fun getTotal(userId: Int):Int
}