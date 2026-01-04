package com.kth.stepapp.core.entities

import com.google.gson.annotations.SerializedName

data class PlayerLoginDto(
    @SerializedName("Email")
    val email: String,

    @SerializedName("Password")
    val password: String
)
