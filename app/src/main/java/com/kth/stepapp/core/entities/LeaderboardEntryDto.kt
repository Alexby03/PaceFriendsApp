package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class LeaderboardEntryDto(
    @SerializedName("PlayerId")
    val playerId: String,

    @SerializedName("FullName")
    val fullName: String,

    @SerializedName("Score")
    val score: Long
)
