package com.kth.stepapp.core.entities

data class LocationUiState(
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0,
    val pathPoints: List<Pair<Double, Double>> = emptyList()
)