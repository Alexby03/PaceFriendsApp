package com.kth.stepapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface DemoViewModel {

    val nrOfSteps: StateFlow<Int>

}

class DemoVM (

): DemoViewModel, ViewModel() {

    private val _nrOfSteps = MutableStateFlow(5)
    override val nrOfSteps: StateFlow<Int>
        get() = _nrOfSteps.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)

                DemoVM (
                    //playerRepository = app.playerRepository
                )
            }
        }
    }

}

class FakeDemoVM: DemoViewModel {
    override val nrOfSteps = MutableStateFlow(1)
}