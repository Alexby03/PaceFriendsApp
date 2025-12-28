package com.kth.stepapp.core.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationService(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {
    private val TAG = "LocationService"

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(intervalMs: Long): Flow<Location> = callbackFlow {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.e(TAG, "CRITICAL: System Location is disabled! No updates will come.")
            close(Exception("System Location Disabled"))
            return@callbackFlow
        }

        Log.d(TAG, "Starting location updates. Interval: ${intervalMs}ms. GPS Active: $isGpsEnabled")

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                if (loc != null) {
                    trySend(loc)
                } else {
                    Log.w(TAG, "Received callback but location was null.")
                }
            }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMs
        ).setMinUpdateDistanceMeters(0f)
            .build()

        try {
            client.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            ).addOnFailureListener { e ->
                Log.e(TAG, "Failed to request updates: ${e.message}")
                close(e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception requesting updates: ${e.message}")
            close(e)
        }

        awaitClose {
            Log.d(TAG, "Stopping location updates (Flow closed).")
            client.removeLocationUpdates(callback)
        }
    }
}
