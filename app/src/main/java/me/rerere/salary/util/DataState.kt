package me.rerere.salary.util

sealed class DataState<out T> {
    fun read(): T = readSafely()!!
    fun readSafely(): T? = if (this is Success) value else null

    class Success<T>(val value: T) : DataState<T>()

    class Error(
        val error: String
    ) : DataState<Nothing>()

    object Loading : DataState<Nothing>()

    object Empty : DataState<Nothing>()
}