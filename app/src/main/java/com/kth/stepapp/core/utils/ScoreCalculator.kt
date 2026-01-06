package com.kth.stepapp.core.utils

import kotlin.math.floor

object ScoreCalculator
{
    private val TIME_FACTOR = 0.75f
    private val CALORIES_FACTOR = 0.15f;
    private val AREA_FACTOR = 0.005f;
    private val STEPS_FACTOR = 0.005f;
    private val WALKING_ACTIVITY_FACTOR = 1.0f;
    private val JOGGING_ACTIVITY_FACTOR = 1.5f;
    private val RUNNING_ACTIVITY_FACTOR = 2.0f;

    fun CalculateScore(totalSteps: Long, totalCalories: Long, timeSpent: Long, areaCovered: Double,
    activity: String): Long
    {
        var activityFactor = WALKING_ACTIVITY_FACTOR;
        if(activity == "Jogging") activityFactor = JOGGING_ACTIVITY_FACTOR;
        else if (activity == "Running") activityFactor = RUNNING_ACTIVITY_FACTOR;

        val calculatedScore = ((timeSpent * TIME_FACTOR) + (totalCalories * CALORIES_FACTOR) +
                (areaCovered * AREA_FACTOR) + (totalSteps * STEPS_FACTOR)) * activityFactor
        return floor(calculatedScore).toLong()
    }
}