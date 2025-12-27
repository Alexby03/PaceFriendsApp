package com.kth.stepapp.core.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

class StepCounter(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
}