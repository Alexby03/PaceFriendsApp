package com.kth.stepapp.data.repository

import android.util.Log
import com.kth.stepapp.data.models.UserProfile

class ProfileRepository {

    fun saveProfile(profile: UserProfile) {
        // TODO: replace with real database call
        Log.d("ProfileRepository", "Saving profile: $profile")
    }
}
