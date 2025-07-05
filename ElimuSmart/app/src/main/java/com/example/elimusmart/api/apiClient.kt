package com.example.elimusmart.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.100.149:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}