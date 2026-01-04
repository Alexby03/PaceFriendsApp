package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class RoutePointDto(
    @SerializedName("Latitude")
    val latitude: Double,

    @SerializedName("Longitude")
    val longitude: Double,

    @SerializedName("SequenceOrder")
    val sequenceOrder: Int
)
