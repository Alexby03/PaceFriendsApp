package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("PlayerId")
    val playerId: String?,

    @SerializedName("FullName")
    val fullName: String,

    @SerializedName("Email")
    val email: String,

    @SerializedName("Password")
    val password: String?,

    @SerializedName("Age")
    val age: Int,

    @SerializedName("HeightCm")
    val heightCm: Double,

    @SerializedName("WeightKg")
    val weightKg: Double,

    @SerializedName("Gender")
    val gender: String,

    @SerializedName("TotalScore")
    val totalScore: Long,

    @SerializedName("CurrentStreak")
    val currentStreak: Int,

    @SerializedName("WeekScore")
    val weekScore: Long
)
