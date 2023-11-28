package ru.yamost.playlistmaker.util

interface Mapper<T, Model> {
    fun toModel(value: T): Model
    fun fromModel(value: Model): T
}