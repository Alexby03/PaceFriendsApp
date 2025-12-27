package com.kth.stepapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.services.CaloriesCalculator
import com.kth.stepapp.core.services.StepCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface DemoViewModel {

    val nrOfSteps: StateFlow<Long>

    val caloriesBurned: StateFlow<Int>

    val walkingTimeSeconds: StateFlow<Long>

}

class DemoVM (
    private val stepCounter: StepCounter,
    private val caloriesCalculator: CaloriesCalculator
): DemoViewModel, ViewModel() {

    private val _nrOfSteps = MutableStateFlow(5678L)
    override val nrOfSteps: StateFlow<Long> = _nrOfSteps.asStateFlow()

    private val _caloriesBurned = MutableStateFlow(0)
    override val caloriesBurned: StateFlow<Int> = _caloriesBurned.asStateFlow()
    private val _walkingTimeSeconds = MutableStateFlow(0L)
    override val walkingTimeSeconds: StateFlow<Long> = _walkingTimeSeconds.asStateFlow()



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)

                DemoVM (
                    stepCounter = app.stepCounter,
                    caloriesCalculator = CaloriesCalculator()
                )
            }
        }
    }

    init {
        Log.d("DemoVM", "ViewModel INIT started!")

        // 1️⃣ Walking time counter (1 second tick)
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1_000)
                _walkingTimeSeconds.value += 1
            }
        }

        viewModelScope.launch {
            Log.d("DemoVM", "Coroutine launched, waiting for sensor...")

            stepCounter.getStepCounts()
                .onStart { Log.d("DemoVM", "Flow started collecting") }
                .catch { e -> Log.e("DemoVM", "Flow error: ${e.message}") }
                .collect { steps ->
                    Log.d("DemoVM","NR OF STEPS $steps")
                    _nrOfSteps.value = steps
                    _caloriesBurned.value = caloriesCalculator.calculateCalories(
                        steps,
                        walkingTimeSeconds = _walkingTimeSeconds.value
                    )
                }
        }
    }
}

class FakeDemoVM: DemoViewModel {
    override val nrOfSteps = MutableStateFlow(1L)

    override val caloriesBurned = MutableStateFlow(180)

    override val walkingTimeSeconds = MutableStateFlow(124L)
}