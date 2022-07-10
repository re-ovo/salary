package me.rerere.salary.api

data class Response<T>(
    val code: Int,
    val message: String,
    val body: T?
)