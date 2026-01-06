package com.kth.stepapp.core.api

import com.kth.stepapp.core.entities.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun registerUser(
        @Body playerDto: PlayerDto
    ): Response<PlayerDto>

    @POST("api/auth/login")
    suspend fun login(
        @Body loginDto: PlayerLoginDto
    ): Response<PlayerDto>

    @PUT("api/profile")
    suspend fun updateProfile(
        @Body playerDto: PlayerDto
    ): Response<PlayerDto>

    @POST("api/activity/sync")
    suspend fun syncActivity(
        @Query("playerId") playerId: String,
        @Body sessionData: DayEntryDto
    ): Response<Map<String, String>>

    @GET("api/calendar")
    suspend fun getCalendar(
        @Query("playerId") playerId: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<List<DaySummaryDto>>

    @GET("api/day")
    suspend fun getDayDetail(
        @Query("date") date: String, //"yyyy-MM-dd"
        @Query("playerId") playerId: String
    ): Response<DayDetailDto>

    @GET("api/leaderboard")
    suspend fun getLeaderboard(): Response<List<LeaderboardEntryDto>>

    @GET("api/winner")
    suspend fun getWinner(): Response<PlayerDto>
}
