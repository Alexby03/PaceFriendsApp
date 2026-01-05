package com.kth.stepapp.data.repositories

import com.google.gson.annotations.SerializedName
import com.kth.stepapp.core.entities.PlayerDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlayerRepository {

    private val _playerId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _fullName: MutableStateFlow<String> = MutableStateFlow("")

    private val _email: MutableStateFlow<String> = MutableStateFlow("")

    private val _password: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _age: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _heightCm: MutableStateFlow<Double> = MutableStateFlow(0.0)

    private val _weightKg: MutableStateFlow<Double> = MutableStateFlow(0.0)

    private val _gender: MutableStateFlow<String> = MutableStateFlow("")

    private val _totalScore: MutableStateFlow<Long> = MutableStateFlow(0)

    private val _currentStreak: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _weekScore: MutableStateFlow<Long> = MutableStateFlow(0)

    val playerId: StateFlow<String?> = _playerId
    val fullName: StateFlow<String> = _fullName.asStateFlow()
    val email: StateFlow<String> = _email.asStateFlow()
    val password: StateFlow<String?> = _password.asStateFlow()
    val age: StateFlow<Int> = _age.asStateFlow()
    val heightCm: StateFlow<Double> = _heightCm.asStateFlow()
    val weightKg: StateFlow<Double> = _weightKg.asStateFlow()
    val gender: StateFlow<String> = _gender.asStateFlow()
    val totalScore: StateFlow<Long> = _totalScore.asStateFlow()
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    val weekScore: StateFlow<Long> = _weekScore.asStateFlow()

    fun login(playerDto: PlayerDto) {
        _playerId.value = playerDto.playerId
        _fullName.value = playerDto.fullName
        _email.value = playerDto.email
        _password.value = playerDto.password
        _age.value = playerDto.age
        _heightCm.value = playerDto.heightCm
        _weightKg.value = playerDto.weightKg
        _gender.value = playerDto.gender
        _totalScore.value = playerDto.totalScore
        _currentStreak.value = playerDto.currentStreak
        _weekScore.value = playerDto.weekScore
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
        _totalScore.value = 0
        _currentStreak.value = 0
        _weekScore.value = 0
    }

}