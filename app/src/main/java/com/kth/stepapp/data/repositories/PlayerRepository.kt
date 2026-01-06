package com.kth.stepapp.data.repositories

import com.kth.stepapp.core.entities.PlayerDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlayerRepository {

    private val _playerId: MutableStateFlow<String?> = MutableStateFlow(null)
    val playerId: StateFlow<String?> = _playerId.asStateFlow()

    private val _fullName: MutableStateFlow<String> = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password: MutableStateFlow<String?> = MutableStateFlow(null)
    val password: StateFlow<String?> = _password.asStateFlow()

    private val _age: MutableStateFlow<Int> = MutableStateFlow(0)
    val age: StateFlow<Int> = _age.asStateFlow()

    private val _heightCm: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val heightCm: StateFlow<Double> = _heightCm.asStateFlow()

    private val _weightKg: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val weightKg: StateFlow<Double> = _weightKg.asStateFlow()

    private val _gender: MutableStateFlow<String> = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _currentStreak: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _completedDaily: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val completedDaily: StateFlow<Boolean> = _completedDaily.asStateFlow()

    private val _weekScore: MutableStateFlow<Long> = MutableStateFlow(0)
    val weekScore: StateFlow<Long> = _weekScore.asStateFlow()

    private val _totalTimePlayed: MutableStateFlow<Long> = MutableStateFlow(0)
    val totalTimePlayed: StateFlow<Long> = _totalTimePlayed.asStateFlow()

    private val _weeklySteps: MutableStateFlow<Long> = MutableStateFlow(0)
    val weeklySteps: StateFlow<Long> = _weeklySteps.asStateFlow()

    private val _lastUpdated: MutableStateFlow<String> = MutableStateFlow("")
    val lastUpdated: StateFlow<String> = _lastUpdated.asStateFlow()

    private val _totalScore: MutableStateFlow<Long> = MutableStateFlow(0)
    val totalScore: StateFlow<Long> = _totalScore.asStateFlow()

    fun login(playerDto: PlayerDto) {
        _playerId.value = playerDto.playerId
        _fullName.value = playerDto.fullName
        _email.value = playerDto.email
        _password.value = playerDto.password
        _age.value = playerDto.age
        _heightCm.value = playerDto.heightCm
        _weightKg.value = playerDto.weightKg
        _gender.value = playerDto.gender
        _currentStreak.value = playerDto.currentStreak
        _completedDaily.value = playerDto.completedDaily
        _weekScore.value = playerDto.weekScore
        _totalTimePlayed.value = playerDto.totalTimePlayed
        _weeklySteps.value = playerDto.weeklySteps
        _lastUpdated.value = playerDto.lastUpdated
        _totalScore.value = playerDto.totalScore
    }

    fun logout() {
        _playerId.value = null
        _fullName.value = ""
        _email.value = ""
        _password.value = null
        _age.value = 0
        _heightCm.value = 0.0
        _weightKg.value = 0.0
        _gender.value = ""
        _currentStreak.value = 0
        _completedDaily.value = false
        _weekScore.value = 0
        _totalTimePlayed.value = 0
        _weeklySteps.value = 0
        _lastUpdated.value = ""
        _totalScore.value = 0
    }

    fun refreshProfile(playerDto: PlayerDto) {
        _fullName.value = playerDto.fullName
        _email.value = playerDto.email
        _age.value = playerDto.age
        _heightCm.value = playerDto.heightCm
        _weightKg.value = playerDto.weightKg
        _gender.value = playerDto.gender
        _currentStreak.value = playerDto.currentStreak
        _completedDaily.value = playerDto.completedDaily
        _weekScore.value = playerDto.weekScore
        _totalTimePlayed.value = playerDto.totalTimePlayed
        _weeklySteps.value = playerDto.weeklySteps
        _lastUpdated.value = playerDto.lastUpdated
        _totalScore.value = playerDto.totalScore
    }

    fun updateProfile(
        fullName: String,
        age: Int,
        heightCm: Double,
        weightKg: Double,
        gender: String
    ) {
        _fullName.value = fullName
        _age.value = age
        _heightCm.value = heightCm
        _weightKg.value = weightKg
        _gender.value = gender
    }

}
