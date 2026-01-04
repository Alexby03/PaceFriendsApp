package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class PlayerLoginDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
