package com.example.elimusmart.api

import com.example.elimusmart.model.Task
import com.example.elimusmart.models.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/api/auth/login")
    fun login(@Body user: User): Call<User>

    @GET("/api/task")
    fun getAllTasks(): Call<List<Task>>

    @PUT("/api/task/{id}/done")
    fun markTaskAsDone(@Path("id") taskId: Long): Call<String>


        @GET("api/tasks/{id}")
        fun getTaskById(@Path("id") id: Long): Call<Task>

        @POST("api/tasks")
        fun createTask(@Body task: Task): Call<Task>

        @PUT("api/tasks/{id}")
        fun updateTask(@Path("id") id: Long, @Body task: Task): Call<Task>

        @DELETE("api/tasks/{id}")
        fun deleteTask(@Path("id") id: Long): Call<Void>
    }


