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
import com.kth.stepapp.core.entities.PlayerLoginDto
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import com.kth.stepapp.data.repositories.PlayerRepository
import com.kth.stepapp.data.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant

interface LoginViewModel {
    val fullName: StateFlow<String>
    val email: StateFlow<String>
    val password: StateFlow<String>
    val age: StateFlow<String>
    val heightCm: StateFlow<String>
    val weightKg: StateFlow<String>
    val gender: StateFlow<String>
    val isSaved: StateFlow<Boolean>
    val error: StateFlow<String?>

    fun onFullNameChange(value: String)
    fun onEmailChange(value: String)
    fun onPasswordChange(value: String)
    fun onAgeChange(value: String)
    fun onHeightChange(value: String)
    fun onWeightChange(value: String)
    fun onGenderChange(value: String)

    fun onRegister()
    fun onLogin()
}

class LoginVM(
    private val paceFriendsRepository: PaceFriendsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : LoginViewModel, ViewModel() {

    //private val repository = ProfileRepository()

    private val _fullName = MutableStateFlow("")
    override val fullName: StateFlow<String> = _fullName

    private val _email = MutableStateFlow("")
    override val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    override val password: StateFlow<String> = _password

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

    override fun onPasswordChange(value: String) {
        _password.value = value
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

    override fun onRegister() {

        val player = try {
            PlayerDto(
                null,
                _fullName.value,
                _email.value,
                _password.value,
                _age.value.toInt(),
                _heightCm.value.toDouble(),
                _weightKg.value.toDouble(),
                _gender.value,
                0,
                false,
                0,
                0,
                0,
                Instant.now().toString(),
                0
            )
        } catch (e: Exception) {
            _error.value = "Invalid input values"
            return
        }


        viewModelScope.launch {
            try {
                val result = paceFriendsRepository.registerUser(player)

                if (result != null) {
                    PlayerRepository.login(PlayerDto(
                        result.playerId,
                        result.fullName,
                        result.email,
                        result.password,
                        result.age,
                        result.heightCm,
                        result.weightKg,
                        result.gender,
                        result.currentStreak,
                        result.completedDaily,
                        result.weekScore,
                        result.totalTimePlayed,
                        result.weeklySteps,
                        result.lastUpdated,
                        result.totalScore
                    ))
                    Log.d("OkHttp", PlayerRepository.playerId.value.toString())
                    _isSaved.value = true
                    _error.value = null
                } else {
                    _error.value = "Registration failed (Server Error)"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    private suspend fun tryLogin(login: PlayerLoginDto) {
        try {
            val result = paceFriendsRepository.login(login.email, login.password)
            if (result != null) {
                PlayerRepository.login(PlayerDto(
                    result.playerId,
                    result.fullName,
                    result.email,
                    result.password,
                    result.age,
                    result.heightCm,
                    result.weightKg,
                    result.gender,
                    result.currentStreak,
                    result.completedDaily,
                    result.weekScore,
                    result.totalTimePlayed,
                    result.weeklySteps,
                    result.lastUpdated,
                    result.totalScore
                ))
                userPreferencesRepository.saveUser(login.email, login.password)
                _isSaved.value = true
                _error.value = null
            } else {
                _error.value = "Login failed (Server Error)"
            }
        } catch (e: Exception) {
            _error.value = "Network error: ${e.message}"
        }
    }

    override fun onLogin() {
        val login = try {
            PlayerLoginDto(
                _email.value,
                _password.value,
            )
        } catch (e: Exception) {
            _error.value = "Invalid input values"
            return
        }

        viewModelScope.launch {
            tryLogin(login)
        }

    }

    init {
        viewModelScope.launch {
            userPreferencesRepository.userCredentials.collect { (email, password) ->
                if(email != null && password != null) {
                    Log.d("Login", "Found old preferences: $email")
                    tryLogin(PlayerLoginDto(email, password))
                } else {
                    Log.d("Login", "No preferences found, resuming to Log-in page.")
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                val repository = PaceFriendsRepository()
                LoginVM(repository, app.userPreferencesRepository)
            }
        }
    }
}

class FakeLoginVM : LoginViewModel {
    override val fullName = MutableStateFlow("Test User")
    override val email = MutableStateFlow("test@test.com")
    override val password = MutableStateFlow("test")
    override val age = MutableStateFlow("25")
    override val heightCm = MutableStateFlow("180")
    override val weightKg = MutableStateFlow("75")
    override val gender = MutableStateFlow("Male")
    override val isSaved = MutableStateFlow(false)
    override val error = MutableStateFlow<String?>(null)

    override fun onFullNameChange(value: String) {}
    override fun onEmailChange(value: String) {}
    override fun onPasswordChange(value: String) {}
    override fun onAgeChange(value: String) {}
    override fun onHeightChange(value: String) {}
    override fun onWeightChange(value: String) {}
    override fun onGenderChange(value: String) {}
    override fun onRegister() {}
    override fun onLogin() {}
}
