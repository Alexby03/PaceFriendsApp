package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth


interface CalendarViewModel {
    val currentMonth: StateFlow<YearMonth>
    val selectedDate: StateFlow<LocalDate?>

    fun onNextMonth()
    fun onPreviousMonth()
    fun onDateSelected(date: LocalDate)
}

class CalendarVM(
    private val app: Application
) : CalendarViewModel, ViewModel() {
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    override val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    override val selectedDate: StateFlow<LocalDate?> = _selectedDate

    override fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    override fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    override fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        // TODO: add logic later
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                CalendarVM(app = app)
            }
        }
    }
}

class FakeCalendarVM: CalendarViewModel {
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    override val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    override val selectedDate: StateFlow<LocalDate?> = _selectedDate

    override fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    override fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    override fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}