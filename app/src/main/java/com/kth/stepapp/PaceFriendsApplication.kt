package com.kth.stepapp

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kth.stepapp.core.services.StepCounter

class PaceFriendsApplication : Application() {

    lateinit var stepCounter: StepCounter
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        stepCounter = StepCounter(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
}