package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface ProfileViewModel {
    val fullName: StateFlow<String?>
    val email: StateFlow<String?>
    val age: StateFlow<Int?>
    val heightCm: StateFlow<Int?>
    val weightKg: StateFlow<Int?>
    val gender: StateFlow<String?>

    fun onEditProfile()
}

class ProfileVM(
    private val app: Application
) : ProfileViewModel, ViewModel() {

    // TODO: Load this from repository when backend/auth is ready
    private val _fullName = MutableStateFlow<String?>(null)
    override val fullName: StateFlow<String?> = _fullName

    private val _email = MutableStateFlow<String?>(null)
    override val email: StateFlow<String?> = _email

    private val _age = MutableStateFlow<Int?>(null)
    override val age: StateFlow<Int?> = _age

    private val _heightCm = MutableStateFlow<Int?>(null)
    override val heightCm: StateFlow<Int?> = _heightCm

    private val _weightKg = MutableStateFlow<Int?>(null)
    override val weightKg: StateFlow<Int?> = _weightKg

    private val _gender = MutableStateFlow<String?>(null)
    override val gender: StateFlow<String?> = _gender

    init {
        // TODO: Replace with real profile loading
        loadFakeProfile()
    }

    private fun loadFakeProfile() {
        _fullName.value = "Test User"
        _email.value = "test@test.com"
        _age.value = 25
        _heightCm.value = 180
        _weightKg.value = 75
        _gender.value = "Not set"
    }

    override fun onEditProfile() {
        // TODO: Navigate to EditProfileScreen later
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                ProfileVM(app = app)
            }
        }
    }
}

class FakeProfileVM: ProfileViewModel {
    override val fullName = MutableStateFlow("Preview User")
    override val email = MutableStateFlow("preview@test.com")
    override val age = MutableStateFlow(30)
    override val heightCm = MutableStateFlow(175)
    override val weightKg = MutableStateFlow(70)
    override val gender = MutableStateFlow("Male")

    override fun onEditProfile() {
        // no-op
    }
}