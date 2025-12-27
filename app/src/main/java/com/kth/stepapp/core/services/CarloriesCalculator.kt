package com.kth.stepapp.core.services

import kotlin.math.roundToInt

class CaloriesCalculator {

    companion object {
        private const val WALKING_MET = 3.5
        private const val WALKING_SPEED_KMH = 5.0

        // Placeholders until user input
        private const val PLACEHOLDER_HEIGHT_CM = 175
        private const val PLACEHOLDER_WEIGHT_KG = 70
        private const val STEP_LENGTH_FACTOR = 0.415
    }

    fun calculateCalories(
        steps: Long,
        walkingTimeSeconds: Long
    ): Int {
        if (steps <= 0 || walkingTimeSeconds <= 0) return 0

        val timeHours = walkingTimeSeconds / 3600.0

        // MET-based calculation (time is the key factor)
        return (WALKING_MET * PLACEHOLDER_WEIGHT_KG * timeHours)
            .roundToInt()
    }
}
