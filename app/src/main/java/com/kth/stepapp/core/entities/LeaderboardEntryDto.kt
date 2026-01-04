package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class LeaderboardEntryDto(
    @SerializedName("playerId")
    val playerId: String,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("score")
    val score: Long
)
