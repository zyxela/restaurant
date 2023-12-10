package com.example.restaurant.repositoryImpl

import com.example.restaurant.data.Database
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepositoryImpl : MenuRepository {
    override suspend fun getMenu(): List<Dish>? = withContext(Dispatchers.IO) {
        val menu = mutableListOf<Dish>()
        val ids = mutableListOf<Int>()
        val getIds = Database().executeQuery("SELECT * FROM menu;")
        getIds.use {
            if (it == null)
                return@withContext null

            while (it.next())
                ids.add(it.getInt("dish_id"))
        }


        val rs = Database().executeQuery("SELECT * FROM dish;")

        rs.use {
            if (it == null)
                return@withContext null

            while (it.next()) {
                if (ids.contains(it.getInt("id"))) {
                    menu.add(
                        Dish(
                            it.getInt("id"),
                            it.getString("dish"),
                            it.getString("description"),
                            it.getInt("price"),
                            it.getBytes("image")
                        )
                    )
                }
            }
        }

        return@withContext menu

    }

    override suspend fun addToCart(ids: List<Int>, userId:Int) {
        Database().addToCart(ids, userId)
        //executeQuery("INSERT INTO client_cart(user_id, dish_id) VALUES ($userId, $ids);")
    }

    override suspend fun checkCart(userId: Int): Boolean {
        Database().executeQuery("SELECT * FROM client_cart where user_id = $userId").use {
            if (it == null)
                return false
            while (it.next()){
                return true
            }
        }
        return false
    }
}