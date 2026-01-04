package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DayDetailDto(
    @SerializedName("DayId")
    val dayId: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Steps")
    val steps: Long,

    @SerializedName("Calories")
    val calories: Long,

    @SerializedName("Score")
    val score: Long,

    @SerializedName("RoutePoints")
    val routePoints: List<RoutePointDto>
)