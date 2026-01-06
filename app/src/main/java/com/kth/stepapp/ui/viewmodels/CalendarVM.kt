package com.kth.stepapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.DayDetailDto
import com.kth.stepapp.core.entities.DaySummaryDto
import com.kth.stepapp.core.entities.PlayerDto
import com.kth.stepapp.core.entities.RoutePointDto
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import com.kth.stepapp.data.repositories.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth


interface CalendarViewModel {
    val currentMonth: StateFlow<YearMonth>
    val selectedDate: StateFlow<LocalDate?>
    val dayDetail: StateFlow<DayDetailDto?>
    val playerDto: StateFlow<String?>

    val daysWithData: StateFlow<Set<LocalDate>>

    fun onNextMonth()
    fun onPreviousMonth()
    fun onDateSelected(date: LocalDate)
}

class CalendarVM(
    private val app: Application,
    private val paceFriendsRepository: PaceFriendsRepository
) : CalendarViewModel, ViewModel() {
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    override val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    override val selectedDate: StateFlow<LocalDate?> = _selectedDate

    private val _dayDetail = MutableStateFlow<DayDetailDto?>(null)
    override val dayDetail: StateFlow<DayDetailDto?> = _dayDetail

    override val playerDto: StateFlow<String?> = PlayerRepository.playerId

    private val _daysWithData = MutableStateFlow<Set<LocalDate>>(emptySet())
    override val daysWithData: StateFlow<Set<LocalDate>> = _daysWithData


    override fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    override fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    override fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date

        val dateString = date.toString()

        viewModelScope.launch {
            val playerId = playerDto.value

            if (playerId.isNullOrBlank()) {
                _dayDetail.value = null
                return@launch
            }

            _dayDetail.value = paceFriendsRepository.getDayDetail(
                date = dateString,
                playerId = playerId
            )
        }
    }

    private fun loadCalendarForMonth() {
        viewModelScope.launch {
            val playerId = PlayerRepository.playerId.value ?: return@launch

            val month = _currentMonth.value

            val calendar: List<DaySummaryDto> =
                paceFriendsRepository.getCalendar(
                    playerId = playerId,
                    year = month.year,
                    month = month.monthValue
                )

            _daysWithData.value = calendar
                .filter { it.completed }
                .map { summary ->
                    LocalDate.parse(summary.date.substring(0, 10))
                }
                .toSet()
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                CalendarVM(
                    app = app,
                    paceFriendsRepository = PaceFriendsRepository()
                )
            }
        }
    }
}

class FakeCalendarVM : CalendarViewModel {

    private val _currentMonth = MutableStateFlow(
        YearMonth.of(2025, 12)
    )
    override val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _selectedDate = MutableStateFlow(
        LocalDate.of(2025, 12, 3)
    )
    override val selectedDate: StateFlow<LocalDate?> = _selectedDate

    override val daysWithData: StateFlow<Set<LocalDate>> =
        MutableStateFlow(
            setOf(
                LocalDate.of(2025, 12, 5),
                LocalDate.of(2025, 12, 6),
                LocalDate.of(2025, 12, 12)
            )
        )

    private val _dayDetail = MutableStateFlow(
        DayDetailDto(
            dayId = "preview-day-id",
            date = "2025-12-03T00:00:00",
            steps = 2854,
            calories = 128,
            score = 1671,
            routePoints = listOf(
                RoutePointDto(
                    latitude = 59.220088,
                    longitude = 17.891648,
                    sequenceOrder = 0
                ),
                RoutePointDto(
                    latitude = 59.220188,
                    longitude = 17.891748,
                    sequenceOrder = 1
                ),
                RoutePointDto(
                    latitude = 59.220288,
                    longitude = 17.891848,
                    sequenceOrder = 2
                )
            )
        )
    )
    override val dayDetail: StateFlow<DayDetailDto?> = _dayDetail

    override val playerDto: StateFlow<String?> =
        MutableStateFlow("preview-player-id")

    override fun onNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    override fun onPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    override fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        // Preview: vi ändrar inte dayDetail dynamiskt
    }
}

