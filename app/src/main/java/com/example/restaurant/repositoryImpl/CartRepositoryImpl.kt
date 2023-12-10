package com.example.restaurant.repositoryImpl

import com.example.restaurant.data.Database
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepositoryImpl : CartRepository {
    override suspend fun getCart(userId: Int): List<Dish>? = withContext(Dispatchers.IO) {
        val cart = mutableListOf<Dish>()
        var ids = IntArray(0)
        val getIds = Database().executeQuery("SELECT * FROM client_cart where user_id = $userId;")
        getIds.use {
            if (it == null)
                return@withContext null

            while (it.next())
                ids = (it.getArray("dish_id").array as Array<*>).map { it.toString().toInt() }
                    .toIntArray()
        }


        val rs = Database().executeQuery("SELECT * FROM dish;")

        rs.use {
            if (it == null)
                return@withContext null

            while (it.next()) {
                if (ids.contains(it.getInt("id"))) {
                    cart.add(
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

        return@withContext cart

    }

    override suspend fun placeAnOrder(userId: Int) {
        val getIds = Database().executeQuery("SELECT * FROM client_cart WHERE user_id = $userId;")
        var cartId = 0
        getIds.use {
            while (it!!.next())
                cartId = it.getInt("id")
        }
        Database().executeQuery("INSERT INTO current_orders VALUES ($cartId)")
    }

    override suspend fun getTotal(userId: Int): Int = withContext(Dispatchers.IO) {


        var ids = IntArray(0)
        val getIds = Database().executeQuery("SELECT * FROM client_cart where user_id = $userId;")
        getIds.use {
            if (it == null)
                return@withContext 0

            while (it.next())
                ids = (it.getArray("dish_id").array as Array<*>).map { it.toString().toInt() }
                    .toIntArray()
        }


        var query = "SELECT SUM(price) AS total_price FROM dish WHERE "

        ids.forEach {
            query += "dish.id = $it OR "
        }
        query =query.dropLast(3)
        query +=";"

        val rs = Database().executeQuery(query)

        rs.use {
            if (it == null)
                return@withContext 0

            while (it.next()) {
                return@withContext it.getInt("total_price")
            }
        }

        return@withContext 0
    }
}