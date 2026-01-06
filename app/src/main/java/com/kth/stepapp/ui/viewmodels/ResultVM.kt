package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.DayEntryDto
import com.kth.stepapp.core.utils.ScoreCalculator
import com.kth.stepapp.data.repositories.PlayerRepository
import com.kth.stepapp.data.repositories.TrackingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.kth.stepapp.data.repositories.PaceFriendsRepository

interface ResultViewModel {

    val steps: StateFlow<Long>
    val timeSeconds: StateFlow<Long>
    val calories: StateFlow<Int>

    fun onDone()
}

class ResultVM(
    private val app: Application,
    private val paceFriendsRepository: PaceFriendsRepository
) : ResultViewModel, ViewModel() {

    override val steps = TrackingRepository.nrOfSteps
    override val timeSeconds = TrackingRepository.walkingTimeSeconds
    override val calories = TrackingRepository.caloriesBurned

    override fun onDone() {
        viewModelScope.launch {
            val playerId = PlayerRepository.playerId.value ?: return@launch

            val sessionData = DayEntryDto(
                date = LocalDate.now().toString(),
                totalSteps = steps.value,
                totalCalories = calories.value.toLong(),
                timeSpentSeconds = timeSeconds.value,
                areaInSquareMeters = TrackingRepository.areaInSqMeters.value,
                score = TrackingRepository.calculateScore(),
                activity = TrackingRepository.activity.value,
                routePoints = emptyList()
            )

            val success = paceFriendsRepository.syncActivity(
                playerId = playerId,
                sessionData = sessionData
            )

            if (success) {
                TrackingRepository.reset()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as PaceFriendsApplication
                ResultVM(
                    app,
                    paceFriendsRepository = PaceFriendsRepository()
                    )
            }
        }
    }
}

class FakeScoreAndMapVM: ResultViewModel {
    override val steps = MutableStateFlow(3421L)
    override val timeSeconds = MutableStateFlow(1800L)
    override val calories = MutableStateFlow(210)

    override fun onDone() {}
}