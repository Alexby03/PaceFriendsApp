package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication


interface LeaderBoardViewModel {

}

class LeaderBoardVM(
    private val app: Application
) : LeaderBoardViewModel, ViewModel() {



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                LeaderBoardVM(app = app)
            }
        }
    }
}

class FakeLeaderBoardVM: LeaderBoardViewModel {

}