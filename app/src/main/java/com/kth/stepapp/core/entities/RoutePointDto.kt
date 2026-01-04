package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class RoutePointDto(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("sequenceOrder")
    val sequenceOrder: Int
)
