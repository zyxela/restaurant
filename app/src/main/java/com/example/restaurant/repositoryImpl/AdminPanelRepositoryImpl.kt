package com.example.restaurant.repositoryImpl

import com.example.restaurant.data.Database
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.AdminPanelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdminPanelRepositoryImpl : AdminPanelRepository {
    override suspend fun addDish(dish: String, description: String, price: Int, image: ByteArray) {
        Database().addDish(dish, description, price, image)
    }

    override suspend fun getOrders(): List<List<Dish>>? = withContext(Dispatchers.IO) {

        val cc = mutableListOf<Int>()

        Database().executeQuery("SELECT * FROM current_orders;").use {
            if (it == null)
                return@withContext null

            while (it.next()) {
                cc.add(it.getInt("order_id"))
            }
        }



        val orders = mutableListOf<MutableList<Dish>>()
        var ids = mutableListOf<IntArray>()

        var query = "SELECT * FROM client_cart WHERE "
        cc.forEach {
            query += "client_cart.id = $it OR "
        }
        query = query.dropLast(3) + ";"

        val getIds = Database().executeQuery(query)
        getIds.use {
            if (it == null)
                return@withContext null

            while (it.next())
                ids.add((it.getArray("dish_id").array as Array<*>).map { map ->
                    map.toString().toInt()
                }
                    .toIntArray())
        }


        val rs = Database().executeQuery("SELECT * FROM dish;")

        rs.use {
            if (it == null)
                return@withContext null
            ids.forEach { id ->
                val cart = mutableListOf<Dish>()
                while (it.next()) {
                    if (id.contains(it.getInt("id"))) {
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
                orders.add(cart)
            }

        }

        return@withContext orders
    }

    override suspend fun acceptOrder(userId:Int, orderId:Int) {
        Database().executeQuery("DELETE FROM client_cart WHERE user_id = $userId; DELETE FROM current_orders WHERE order_id = $orderId ;")


    }


}