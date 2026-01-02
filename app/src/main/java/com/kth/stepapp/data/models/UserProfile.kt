package com.kth.stepapp.data.models

data class UserProfile(
    val fullName: String,
    val email: String,
    val age: Int,
    val heightCm: Double,
    val weightKg: Double,
    val gender: String
)