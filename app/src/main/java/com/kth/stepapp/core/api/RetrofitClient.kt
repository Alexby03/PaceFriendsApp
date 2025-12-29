package com.kth.stepapp.core.api

import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://tile.openstreetmap.org/"

    val osmService: OsmApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(OsmApiService::class.java)
    }
}