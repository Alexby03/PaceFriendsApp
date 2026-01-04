package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class DaySummaryDto(
    @SerializedName("DayId")
    val dayId: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Steps")
    val steps: Long,

    @SerializedName("Score")
    val score: Long,

    @SerializedName("Completed")
    val completed: Boolean
)
