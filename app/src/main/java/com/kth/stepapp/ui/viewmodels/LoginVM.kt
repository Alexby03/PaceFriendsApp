package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.data.models.UserProfile
import com.kth.stepapp.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val fullName: StateFlow<String>
    val email: StateFlow<String>
    val age: StateFlow<String>
    val heightCm: StateFlow<String>
    val weightKg: StateFlow<String>
    val gender: StateFlow<String>
    val isSaved: StateFlow<Boolean>
    val error: StateFlow<String?>

    fun onFullNameChange(value: String)
    fun onEmailChange(value: String)
    fun onAgeChange(value: String)
    fun onHeightChange(value: String)
    fun onWeightChange(value: String)
    fun onGenderChange(value: String)

    fun onSubmit()
}

class LoginVM(
    private val app: Application
) : LoginViewModel, ViewModel() {

    private val repository = ProfileRepository()

    private val _fullName = MutableStateFlow("")
    override val fullName: StateFlow<String> = _fullName

    private val _email = MutableStateFlow("")
    override val email: StateFlow<String> = _email

    private val _age = MutableStateFlow("")
    override val age: StateFlow<String> = _age

    private val _heightCm = MutableStateFlow("")
    override val heightCm: StateFlow<String> = _heightCm

    private val _weightKg = MutableStateFlow("")
    override val weightKg: StateFlow<String> = _weightKg

    private val _gender = MutableStateFlow("")
    override val gender: StateFlow<String> = _gender

    private val _isSaved = MutableStateFlow(false)
    override val isSaved: StateFlow<Boolean> = _isSaved

    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error

    override fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    override fun onEmailChange(value: String) {
        _email.value = value
    }

    override fun onAgeChange(value: String) {
        _age.value = value
    }

    override fun onHeightChange(value: String) {
        _heightCm.value = value
    }

    override fun onWeightChange(value: String) {
        _weightKg.value = value
    }

    override fun onGenderChange(value: String) {
        _gender.value = value
    }

    override fun onSubmit() {
        try {
            val profile = UserProfile(
                fullName = _fullName.value,
                email = _email.value,
                age = _age.value.toInt(),
                heightCm = _heightCm.value.toDouble(),
                weightKg = _weightKg.value.toDouble(),
                gender = _gender.value
            )

            repository.saveProfile(profile)
            _isSaved.value = true
            _error.value = null

        } catch (e: Exception) {
            _error.value = "Invalid input values"
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                LoginVM(app = app)
            }
        }
    }
}

class FakeLoginVM : LoginViewModel {
    override val fullName = MutableStateFlow("Test User")
    override val email = MutableStateFlow("test@test.com")
    override val age = MutableStateFlow("25")
    override val heightCm = MutableStateFlow("180")
    override val weightKg = MutableStateFlow("75")
    override val gender = MutableStateFlow("Male")
    override val isSaved = MutableStateFlow(false)
    override val error = MutableStateFlow<String?>(null)

    override fun onFullNameChange(value: String) {}
    override fun onEmailChange(value: String) {}
    override fun onAgeChange(value: String) {}
    override fun onHeightChange(value: String) {}
    override fun onWeightChange(value: String) {}
    override fun onGenderChange(value: String) {}
    override fun onSubmit() {}
}
