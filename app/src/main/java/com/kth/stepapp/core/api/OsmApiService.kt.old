package com.kth.stepapp.core.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

interface OsmApiService {

    // https://tile.openstreetmap.org/{z}/{x}/{y}.png

    @Streaming
    @GET("{z}/{x}/{y}.png")
    @Headers("User-Agent: PaceFriendsApp/1.0 (alexanderby03@gmail.com)")
    suspend fun getMapTile(
        @Path("z") zoom: Int,
        @Path("x") x: Int,
        @Path("y") y: Int
    ): Response<ResponseBody>
}
