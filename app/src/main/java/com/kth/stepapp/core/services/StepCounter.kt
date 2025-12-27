package com.kth.stepapp.core.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StepCounter(context: Context) {
    private val sensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) }

    fun getStepCounts(): Flow<Long> = callbackFlow {
        if (sensor == null) {
            Log.e("DemoVM", "CRITICAL: This device has NO Step Counter sensor!")
        } else {
            Log.d("DemoVM", "Sensor found: ${sensor!!.name}")
        }

        var initialSteps: Long? = null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type != Sensor.TYPE_STEP_COUNTER) return
                val currentSteps = event.values[0].toLong()
                if (initialSteps == null) {
                    initialSteps = currentSteps
                }
                val sessionSteps = currentSteps - initialSteps
                trySend(sessionSteps)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener (
            listener, sensor, SensorManager.SENSOR_DELAY_UI
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}