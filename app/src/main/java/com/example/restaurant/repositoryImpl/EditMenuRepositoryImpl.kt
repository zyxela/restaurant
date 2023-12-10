package com.example.restaurant.repositoryImpl

import com.example.restaurant.data.Database
import com.example.restaurant.entities.Dish
import com.example.restaurant.repository.EditMenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditMenuRepositoryImpl : EditMenuRepository {
    override suspend fun addDishToMenu(dishId: Int) {
        Database().executeQuery("INSERT INTO menu Values($dishId)")
    }

    override suspend fun editDish(dish: Dish) {
        Database().executeQuery("UPDATE dish SET dish = '${dish.name}', description = '${dish.description}', price = ${dish.price} WHERE id = ${dish.id};")
    }

    override suspend fun removeDishToMenu(dish: Dish) {
        Database().executeQuery("DELETE FROM menu where dish_id = ${dish.id}")
    }

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

    override suspend fun getDishes(): List<Dish>? = withContext(Dispatchers.IO) {

        val menu = mutableListOf<Dish>()
        val rs = Database().executeQuery("SELECT * FROM dish;")

        rs.use {
            if (it == null)
                return@withContext null

            while (it.next()) {

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

        return@withContext menu
    }

}