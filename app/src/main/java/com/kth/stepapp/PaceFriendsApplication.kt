package com.kth.stepapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kth.stepapp.core.services.LocationService
import com.kth.stepapp.core.services.StepCounter
import com.kth.stepapp.data.repositories.UserPreferencesRepository
import org.osmdroid.config.Configuration

private const val APP_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

class PaceFriendsApplication : Application() {

    lateinit var stepCounter: StepCounter
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationService: LocationService
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = "AndroidApp"
        stepCounter = StepCounter(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = LocationService(this, fusedLocationClient)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}
