package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DaySummaryDto(
    @SerializedName("dayId")
    val dayId: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("steps")
    val steps: Long,

    @SerializedName("score")
    val score: Long,

    @SerializedName("completed")
    val completed: Boolean
)
