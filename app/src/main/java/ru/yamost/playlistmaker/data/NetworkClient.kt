package ru.yamost.playlistmaker.data

import ru.yamost.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doSearchTrackRequest(dto: Any): Response
}