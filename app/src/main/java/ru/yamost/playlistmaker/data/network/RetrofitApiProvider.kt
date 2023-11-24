package ru.yamost.playlistmaker.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object RetrofitApiProvider {
    private const val BASE_URL = "https://itunes.apple.com"
    private const val TIMEOUT_IN_SECOND = 10L
    val tracksService: TracksService by lazy { buildRetrofit().create() }

    private fun buildRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}