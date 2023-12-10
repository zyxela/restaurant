package com.example.restaurant.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Database {

    private var connection: Connection? = null

    private val host = "10.0.2.2"

    private val database = "restaurant"
    private val port = 5432
    private val user = "postgres"
    private val pass = ""
    private var url = "jdbc:postgresql://%s:%d/%s"


    suspend fun executeQuery(query: String): ResultSet? = suspendCoroutine { continuation ->
        val rs = null
        url = String.format(url, host, port, database)
        GlobalScope.launch {
            try {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(url, user, pass)
                val statement = connection?.createStatement()

                continuation.resume(statement?.executeQuery(query))
            } catch (e: Exception) {
                Log.e("executeQuery", "${e.message} : ${e.cause} : ${e.stackTrace}")
                continuation.resume(rs)
            }

        }

    }

    suspend fun addDish(dish: String, description: String, price: Int, image: ByteArray) {
        CoroutineScope(Dispatchers.IO).launch {
            url = String.format(url, host, port, database)
            try {
                DriverManager.getConnection(url, user, pass).use { connection ->
                    val sql =
                        "INSERT INTO dish (dish, description, price, image) VALUES (?, ?, ?, ?);"
                    connection.prepareStatement(sql).use { preparedStatement ->
                        preparedStatement.setString(1, dish)
                        preparedStatement.setString(2, description)
                        preparedStatement.setInt(3, price)
                        preparedStatement.setBytes(4, image)

                        preparedStatement.execute()
                    }
                }
            } catch (e: Exception) {
                Log.e("executeQuery", "${e.message} : ${e.cause} : ${e.stackTrace}")
            }
        }
    }

    suspend fun addToCart(ids: List<Int>, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            url = String.format(url, host, port, database)
            try {
                DriverManager.getConnection(url, user, pass).use { connection ->
                    val sql = "INSERT INTO client_cart(user_id, dish_id) VALUES (?, ?);"
                    connection.prepareStatement(sql).use { preparedStatement ->
                        preparedStatement.setInt(1, id)

                        // Pass the array directly to createArrayOf
                        preparedStatement.setArray(
                            2, connection.createArrayOf(
                                "integer",
                                ids.toTypedArray()  // Corrected this line
                            )
                        )

                        preparedStatement.execute()
                    }
                }
            } catch (e: Exception) {
                Log.e("executeQuery", "${e.message} : ${e.cause} : ${e.stackTrace}")
            }
        }
    }
}