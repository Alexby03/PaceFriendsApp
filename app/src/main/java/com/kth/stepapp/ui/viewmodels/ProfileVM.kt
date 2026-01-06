package com.kth.stepapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.PlayerDto
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import com.kth.stepapp.data.repositories.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface ProfileViewModel {

        val fullName: StateFlow<String?>
        val email: StateFlow<String?>
        val age: StateFlow<Int?>
        val heightCm: StateFlow<Double?>
        val weightKg: StateFlow<Double?>
        val gender: StateFlow<String?>

        val isEditing: StateFlow<Boolean>

        fun onEditProfile()
        fun onSaveProfile()
        fun onCancelEdit()

        fun onFullNameChange(value: String)
        fun onAgeChange(value: Int)
        fun onHeightChange(value: Double)
        fun onWeightChange(value: Double)
        fun onGenderChange(value: String)
}

class ProfileVM(
    private val paceFriendsRepository: PaceFriendsRepository,
) : ProfileViewModel, ViewModel() {

    private val _isEditing = MutableStateFlow(false)
    override val isEditing: StateFlow<Boolean> = _isEditing

    private val _fullName = MutableStateFlow(PlayerRepository.fullName.value)
    override val fullName: StateFlow<String> = _fullName

    override val email: StateFlow<String> = PlayerRepository.email

    private val _age = MutableStateFlow(PlayerRepository.age.value)
    override val age: StateFlow<Int> = _age

    private val _heightCm = MutableStateFlow(PlayerRepository.heightCm.value)
    override val heightCm: StateFlow<Double> = _heightCm

    private val _weightKg = MutableStateFlow(PlayerRepository.weightKg.value)
    override val weightKg: StateFlow<Double> = _weightKg

    private val _gender = MutableStateFlow(PlayerRepository.gender.value)
    override val gender: StateFlow<String> = _gender

    override fun onEditProfile() {
        _isEditing.value = true
    }

    override fun onCancelEdit() {
        _fullName.value = PlayerRepository.fullName.value
        _age.value = PlayerRepository.age.value
        _heightCm.value = PlayerRepository.heightCm.value
        _weightKg.value = PlayerRepository.weightKg.value
        _gender.value = PlayerRepository.gender.value
        _isEditing.value = false
    }

    override fun onSaveProfile() {
        viewModelScope.launch {
            try {
                val result = paceFriendsRepository.updateUser(PlayerDto(
                    PlayerRepository.playerId.value,
                    _fullName.value,
                    PlayerRepository.email.value,
                    null,
                    _age.value,
                    _heightCm.value,
                    _weightKg.value,
                    _gender.value,
                    PlayerRepository.currentStreak.value,
                    PlayerRepository.completedDaily.value,
                    PlayerRepository.weekScore.value,
                    PlayerRepository.totalTimePlayed.value,
                    PlayerRepository.weeklySteps.value,
                    PlayerRepository.lastUpdated.value,
                    PlayerRepository.totalScore.value,
                ))
                if(result != null) {
                    PlayerRepository.updateProfile(
                        result.fullName,
                        result.age,
                        result.heightCm,
                        result.weightKg,
                        result.gender
                    )
                    _isEditing.value = false
                }
            } catch (e: Exception) {
                _isEditing.value = false
                Log.e("Profile", "Failed to update profile", e)
            }
        }
    }

    override fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    override fun onAgeChange(value: Int) {
        _age.value = value
    }

    override fun onHeightChange(value: Double) {
        _heightCm.value = value
    }

    override fun onWeightChange(value: Double) {
        _weightKg.value = value
    }

    override fun onGenderChange(value: String) {
        _gender.value = value
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as PaceFriendsApplication
                ProfileVM(paceFriendsRepository = PaceFriendsRepository())
            }
        }
    }
}


class FakeProfileVM : ProfileViewModel {

    override val isEditing = MutableStateFlow(false)

    override val fullName = MutableStateFlow("Preview User")
    override val email = MutableStateFlow("preview@test.com")
    override val age = MutableStateFlow(30)
    override val heightCm = MutableStateFlow(175.0)
    override val weightKg = MutableStateFlow(70.0)
    override val gender = MutableStateFlow("Male")

    override fun onEditProfile() {
        isEditing.value = true
    }

    override fun onSaveProfile() {
        isEditing.value = false
    }

    override fun onCancelEdit() {
        isEditing.value = false
    }

    override fun onFullNameChange(value: String) {}
    override fun onAgeChange(value: Int) {}
    override fun onHeightChange(value: Double) {}
    override fun onWeightChange(value: Double) {}
    override fun onGenderChange(value: String) {}
}
