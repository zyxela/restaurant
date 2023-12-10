package com.example.restaurant.repositoryImpl

import android.content.Context
import com.example.restaurant.data.Database
import com.example.restaurant.repository.EnterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnterRepositoryImpl : EnterRepository {
    override suspend fun auth(login: String, password: String, context: Context): Boolean =
        withContext(Dispatchers.IO) {
            val db = Database()

            val sharedPreferences =
                context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

            val response =
                db.executeQuery("SELECT * FROM users WHERE login = '$login' AND password = '$password';")
            response?.use {
                while (it.next()) {
                    if (it.getString("login") == login && it.getString("password") == password) {
                        val userid = it.getString("id").toInt()
                        sharedPreferences.edit().putInt("USER_ID", userid).apply()
                        val status = it.getString("role") == "2"
                        sharedPreferences.edit().putBoolean("USER_STATUS", status).apply()
                        return@withContext true
                    }
                }
            }
            return@withContext false
        }


    override suspend fun regist(login: String, password: String, context: Context): Boolean = withContext(Dispatchers.IO) {
        val db = Database()

        val sharedPreferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val response = db.executeQuery("SELECT * FROM users WHERE login = '$login';")
        response?.use { it ->
            while (it.next()) {
                if (it.getString("login") == login) {
                    return@withContext false
                }
            }
            db.executeQuery("INSERT INTO users (login, password, role) VALUES ('$login', '$password', 2);")
            db.executeQuery("SELECT * FROM users WHERE login = '$login';")?.use { users->
                while (users.next()) {
                    if (users.getString("login") == login) {
                        val userid = users.getString("id").toInt()
                        sharedPreferences.edit().putInt("USER_ID", userid).apply()
                        val status = users.getString("role") == "2"
                        sharedPreferences.edit().putBoolean("USER_STATUS", status).apply()
                    }
                }
            }
        }
        return@withContext true
    }

}