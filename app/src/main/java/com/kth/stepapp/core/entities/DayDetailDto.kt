package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DayDetailDto(
    @SerializedName("dayId")
    val dayId: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("steps")
    val steps: Long,

    @SerializedName("calories")
    val calories: Long,

    @SerializedName("score")
    val score: Long,

    @SerializedName("routePoints")
    val routePoints: List<RoutePointDto>
)