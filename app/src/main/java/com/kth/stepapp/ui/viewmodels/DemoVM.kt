package com.kth.stepapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.services.StepCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface DemoViewModel {

    val nrOfSteps: StateFlow<Long>

}

class DemoVM (
    private val stepCounter: StepCounter
): DemoViewModel, ViewModel() {

    private val _nrOfSteps = MutableStateFlow(5678L)
    override val nrOfSteps: StateFlow<Long> = _nrOfSteps.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)

                DemoVM (
                    stepCounter = app.stepCounter
                )
            }
        }
    }

    init {
        Log.d("DemoVM", "ViewModel INIT started!")

        viewModelScope.launch {
            Log.d("DemoVM", "Coroutine launched, waiting for sensor...")

            stepCounter.getStepCounts()
                .onStart { Log.d("DemoVM", "Flow started collecting") }
                .catch { e -> Log.e("DemoVM", "Flow error: ${e.message}") }
                .collect { steps ->
                    Log.d("DemoVM","NR OF STEPS $steps")
                    _nrOfSteps.value = steps
                }
        }
    }

}

class FakeDemoVM: DemoViewModel {
    override val nrOfSteps = MutableStateFlow(1L)
}