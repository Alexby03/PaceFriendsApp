package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("playerId")
    val playerId: String?,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String?,

    @SerializedName("age")
    val age: Int,

    @SerializedName("heightCm")
    val heightCm: Double,

    @SerializedName("weightKg")
    val weightKg: Double,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("currentStreak")
    val currentStreak: Int,

    @SerializedName("completedDaily")
    val completedDaily: Boolean,

    @SerializedName("weekScore")
    val weekScore: Long,

    @SerializedName("totalTimePlayed")
    val totalTimePlayed: Long,

    @SerializedName("weeklySteps")
    val weeklySteps: Long,

    @SerializedName("lastUpdated")
    val lastUpdated: String,

    @SerializedName("totalScore")
    val totalScore: Long
)
