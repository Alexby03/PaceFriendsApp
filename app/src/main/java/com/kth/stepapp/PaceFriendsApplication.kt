package com.kth.stepapp

import android.app.Application
import com.kth.stepapp.core.services.StepCounter

class PaceFriendsApplication : Application() {

    lateinit var stepCounter: StepCounter

    override fun onCreate() {
        super.onCreate()
        stepCounter = StepCounter(this)
    }
}