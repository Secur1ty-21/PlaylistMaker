package ru.yamost.playlistmaker.search.data

import ru.yamost.playlistmaker.search.data.network.dto.Response

interface NetworkClient {
    fun doSearchTrackRequest(dto: Any): Response
}