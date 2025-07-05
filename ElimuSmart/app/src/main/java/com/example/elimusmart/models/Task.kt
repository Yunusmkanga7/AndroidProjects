package com.example.elimusmart.model

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String,
    val deadline: String? = null,
    val done: String? = "not shown"
)