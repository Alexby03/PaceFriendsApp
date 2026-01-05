package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.data.repositories.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ProfileViewModel {

        val fullName: StateFlow<String?>
        val email: StateFlow<String?>
        val age: StateFlow<Int?>
        val heightCm: StateFlow<Double?>
        val weightKg: StateFlow<Double?>
        val gender: StateFlow<String?>

        fun onEditProfile()

}

class ProfileVM(
    private val app: Application
) : ProfileViewModel, ViewModel() {

    override val fullName: StateFlow<String> = PlayerRepository.fullName
    override val email: StateFlow<String> = PlayerRepository.email
    override val age: StateFlow<Int> = PlayerRepository.age
    override val heightCm: StateFlow<Double> = PlayerRepository.heightCm
    override val weightKg: StateFlow<Double> = PlayerRepository.weightKg
    override val gender: StateFlow<String> = PlayerRepository.gender

    override fun onEditProfile() {
        // TODO: Navigate to EditProfileScreen
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as PaceFriendsApplication
                ProfileVM(app)
            }
        }
    }
}

class FakeProfileVM: ProfileViewModel {
    override val fullName = MutableStateFlow("Preview User")
    override val email = MutableStateFlow("preview@test.com")
    override val age = MutableStateFlow(30)
    override val heightCm = MutableStateFlow(175.0)
    override val weightKg = MutableStateFlow(70.0)
    override val gender = MutableStateFlow("Male")

    override fun onEditProfile() {
        // no-op
    }
}