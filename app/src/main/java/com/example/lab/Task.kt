package com.example.lab

data class Task(
    val id: Int,
    val title: String,
    val done: Boolean = false
)