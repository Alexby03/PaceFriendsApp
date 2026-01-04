package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication


interface ScoreAndMapViewModel {

}

class ScoreAndMapVM(
    private val app: Application
) : ScoreAndMapViewModel, ViewModel() {



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                ScoreAndMapVM(app = app)
            }
        }
    }
}

class FakeScoreAndMapVM: ScoreAndMapViewModel {

}