package ru.yamost.playlistmaker.data.network

interface ResultCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(error: Throwable)
}