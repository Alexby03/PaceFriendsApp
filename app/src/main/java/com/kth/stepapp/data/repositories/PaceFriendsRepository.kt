package com.kth.stepapp.data.repositories

import android.util.Log
import com.kth.stepapp.core.entities.*
import com.kth.stepapp.core.api.RetrofitClient

class PaceFriendsRepository {

    private val api = RetrofitClient.apiService

    // =================================================================
    // Register User
    // =================================================================
    suspend fun registerUser(dto: PlayerDto): PlayerDto? {
        return try {
            val response = api.registerUser(dto)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("Repo", "Register failed: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Repo", "Register error", e)
            null
        }
    }

    // =================================================================
    // Login
    // =================================================================
    suspend fun login(email: String, pass: String): PlayerDto? {
        return try {
            val request = PlayerLoginDto(email = email, password = pass)
            val response = api.login(request)

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("Repo", "Login failed: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Repo", "Login error", e)
            null
        }
    }

    // =================================================================
    // Update User
    // =================================================================
    suspend fun updateUser(dto: PlayerDto): PlayerDto? {
        return try {
            val response = api.updateProfile(dto)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("Repo", "Update error", e)
            null
        }
    }

    // =================================================================
    // Sync Activity (Add/Update Day)
    // =================================================================
    suspend fun syncActivity(playerId: String, sessionData: DayEntryDto): Boolean {
        return try {
            val response = api.syncActivity(playerId, sessionData)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("Repo", "Sync error", e)
            false
        }
    }

    // =================================================================
    // Get Calendar
    // =================================================================
    suspend fun getCalendar(playerId: String, year: Int, month: Int): List<DaySummaryDto> {
        return try {
            val response = api.getCalendar(playerId, year, month)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repo", "Calendar error", e)
            emptyList()
        }
    }

    // =================================================================
    // Get Day Detail
    // =================================================================
    suspend fun getDayDetail(date: String, playerId: String): DayDetailDto? {
        return try {
            val response = api.getDayDetail(date, playerId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("Repo", "Day Detail error", e)
            null
        }
    }

    // =================================================================
    // Get Leaderboard
    // =================================================================
    suspend fun getLeaderboard(): List<LeaderboardEntryDto> {
        return try {
            val response = api.getLeaderboard()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repo", "Leaderboard error", e)
            emptyList()
        }
    }
}
