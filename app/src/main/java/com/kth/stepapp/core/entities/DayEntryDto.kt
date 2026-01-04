package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DayEntryDto(
    @SerializedName("Date")
    val date: String,

    @SerializedName("TotalSteps")
    val totalSteps: Long,

    @SerializedName("TotalCalories")
    val totalCalories: Long,

    @SerializedName("TimeSpentSeconds")
    val timeSpentSeconds: Long,

    @SerializedName("AreaInSquareMeters")
    val areaInSquareMeters: Double,

    @SerializedName("Score")
    val score: Long,

    @SerializedName("Activity")
    val activity: String,

    @SerializedName("RoutePoints")
    val routePoints: List<RoutePointDto>
)