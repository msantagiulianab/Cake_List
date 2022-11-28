package com.example.cakelist.sealed

sealed class DataState<out T, out E> {
    class Success<T>(val data: T) : DataState<T, Nothing>()
    class Failure<E>(val message: E) : DataState<Nothing, E>()
    object Loading : DataState<Nothing, Nothing>()
    object Empty : DataState<Nothing, Nothing>()
}
