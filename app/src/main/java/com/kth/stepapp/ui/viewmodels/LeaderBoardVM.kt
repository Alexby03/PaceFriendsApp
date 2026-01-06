package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.LeaderboardEntryDto
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


interface LeaderBoardViewModel {
    val leaderboard: StateFlow<List<LeaderboardEntryDto>>
    val isLoading: StateFlow<Boolean>
}

class LeaderBoardVM(
    private val app: Application,
    private val paceFriendsRepository: PaceFriendsRepository
) : LeaderBoardViewModel, ViewModel() {

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntryDto>>(emptyList())
    override val leaderboard: StateFlow<List<LeaderboardEntryDto>> = _leaderboard

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _leaderboard.value = paceFriendsRepository.getLeaderboard()
            _isLoading.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                LeaderBoardVM(
                    app = app,
                    paceFriendsRepository = PaceFriendsRepository()
                )
            }
        }
    }
}

class FakeLeaderBoardVM: LeaderBoardViewModel {

    override val leaderboard = MutableStateFlow(
        listOf(
            LeaderboardEntryDto("1", "Noah", 3450),
            LeaderboardEntryDto("2", "Alex", 2980),
            LeaderboardEntryDto("3", "Emma", 2710)
        )
    )

    override val isLoading = MutableStateFlow(false)
}