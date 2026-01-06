package com.kth.stepapp.core.services

import com.kth.stepapp.data.repositories.PlayerRepository
import kotlin.math.roundToInt

class CaloriesCalculator {

    companion object {
        private const val WALKING_MET = 3.5
        private const val JOGGING_MET = 7.0
        private const val RUNNING_MET = 10.0
    }

    fun calculateCalories(
        steps: Long,
        walkingTimeSeconds: Long,
        activity: String
    ): Int {
        if (steps <= 0 || walkingTimeSeconds <= 0) return 0

        val timeHours = walkingTimeSeconds / 3600.0

        val metValue = when (activity) {
            "Walking" -> WALKING_MET
            "Jogging" -> JOGGING_MET
            "Running" -> RUNNING_MET
            else -> WALKING_MET
        }

        return (metValue * PlayerRepository.weightKg.value * timeHours).roundToInt()
    }
}
