package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DayEntryDto(
    @SerializedName("date")
    val date: String,

    @SerializedName("totalSteps")
    val totalSteps: Long,

    @SerializedName("totalCalories")
    val totalCalories: Long,

    @SerializedName("timeSpentSeconds")
    val timeSpentSeconds: Long,

    @SerializedName("areaInSquareMeters")
    val areaInSquareMeters: Double,

    @SerializedName("score")
    val score: Long,

    @SerializedName("activity")
    val activity: String,

    @SerializedName("routePoints")
    val routePoints: List<RoutePointDto>
)