package com.example.restaurant.repository

import com.example.restaurant.entities.Dish

interface AdminPanelRepository {
    suspend fun addDish(dish:String, description:String, price:Int, image:ByteArray)
    suspend fun getOrders():List<List<Dish>>?
}