package com.kth.stepapp.ui.viewmodels

import kotlinx.coroutines.flow.StateFlow
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.PlayerLoginDto
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import com.kth.stepapp.data.repositories.PlayerRepository
import com.kth.stepapp.data.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface HomeViewModel {
    val fullName: StateFlow<String>
    val weeklySteps: StateFlow<Long>
    val weeklyScore: StateFlow<Long>
    val streak: StateFlow<Int>
    val daylilyComplete: StateFlow<Boolean>
    fun logoutPlayer()
}

class HomeVM (
    private val paceFriendsRepository: PaceFriendsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
    ) : HomeViewModel, ViewModel() {

    override val fullName: StateFlow<String> = PlayerRepository.fullName

    override val weeklySteps: StateFlow<Long> = PlayerRepository.weeklySteps

    override val weeklyScore: StateFlow<Long> = PlayerRepository.weekScore

    override val streak: StateFlow<Int> = PlayerRepository.currentStreak

    override val daylilyComplete: StateFlow<Boolean> = PlayerRepository.completedDaily

    override fun logoutPlayer() {
        PlayerRepository.logout()
        viewModelScope.launch { userPreferencesRepository.clearUser() }
    }

    init {
        viewModelScope.launch{
            val refreshedProfile = paceFriendsRepository.getPlayer(PlayerRepository.playerId.value.toString())
            if(refreshedProfile != null) PlayerRepository.refreshProfile(refreshedProfile)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                HomeVM(paceFriendsRepository = PaceFriendsRepository(), app.userPreferencesRepository)
            }
        }
    }
}

class FakeHomeVM : HomeViewModel {

    override val fullName = MutableStateFlow("Noah")
    override val weeklySteps = MutableStateFlow(42_000L)
    override val weeklyScore = MutableStateFlow(12_340L)
    override val streak = MutableStateFlow(5)
    override val daylilyComplete = MutableStateFlow(true)
    override fun logoutPlayer() { }
}
