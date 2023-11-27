package ru.yamost.playlistmaker.util

sealed class Resource<T, E> {
    class Success<T, E>(val data: T) : Resource<T, E>()
    class Error<T, E>(val errorStatus: E, val data: T? = null) : Resource<T, E>()
}