package com.example.restaurant.repository

import android.content.Context

interface EnterRepository {
    suspend fun auth(login:String, password:String, context: Context):Boolean
    suspend fun regist(login:String, password:String, context: Context): Boolean
}