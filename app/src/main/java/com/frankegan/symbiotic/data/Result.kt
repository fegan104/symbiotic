package com.frankegan.symbiotic.data

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()
}

fun <T : Any> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> this.data
    is Result.Error -> null
}

inline fun <T : Any, R : Any> Result<T>.map(crossinline transform: (T) -> R): Result<R> =
    when (this) {
        is Result.Success -> Result.Success(transform(this.data))
        is Result.Error -> this
    }