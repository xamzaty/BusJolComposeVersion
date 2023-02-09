package kz.busjol.presentation.passenger.buy_ticket.journey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.busjol.BuildConfig
import kz.busjol.domain.models.Seats
import kz.busjol.domain.repository.SeatsListRepository
import kz.busjol.domain.util.Resource
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import javax.inject.Inject

@HiltViewModel
class JourneyViewModel @Inject constructor(
    private val seatsListRepository: SeatsListRepository
): ViewModel() {

    var state by mutableStateOf(JourneyState().mock())
        private set

    fun onEvent(event: JourneyEvent) {
        state = when (event) {
            is JourneyEvent.SelectedOption -> {
                state.copy(
                    selectedOption = event.value
                )
            }
            is JourneyEvent.OnJourneyClicked -> {
                loadSeatsList(event.id)
                state.copy(
                    selectedJourney = event.selectedJourney
                )
            }
            is JourneyEvent.NewDestinationStatus -> {
                state.copy(
                    startNewDestination = event.isStarted
                )
            }
        }
    }

    private fun loadSeatsList(id: String) {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) {
                state = state.copy(
                    seatsList = mockSeatList(),
                    isLoading = false,
                    error = null,
                    startNewDestination = true
                )
            } else {
                seatsListRepository
                    .getSeatsList(id)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                state = state.copy(
                                    seatsList = result.data,
                                    isLoading = false,
                                    error = null,
                                    startNewDestination = true
                                )
                            }
                            is Resource.Error -> {
                                state = state.copy(
                                    seatsList = null,
                                    isLoading = false,
                                    error = result.message,
                                    startNewDestination = false
                                )
                            }
                            is Resource.Loading -> {
                                state = state.copy(
                                    isLoading = true
                                )
                            }
                        }
                    }
            }
        }
    }
}

private fun mockSeatList() = listOf(
    Seats(0, 0, "1", true, 1, "",0, ""),
    Seats(0, 0, "2", true, 2, "",0, ""),
    Seats(0, 0, "3", true, 3, "",0, ""),
    Seats(0, 0, "4", false, 4, "",0, ""),
    Seats(0, 0, "5", true, 5, "",0, ""),
    Seats(0, 0, "6", true, 6, "",0, ""),
    Seats(0, 0, "7", true, 7, "",0, ""),
    Seats(0, 0, "8", false, 8, "",0, ""),
    Seats(0, 0, "9", true, 9, "",0, ""),
    Seats(0, 0, "10", true, 10, "",0, ""),
    Seats(0, 0, "11", true, 11, "",0, ""),
    Seats(0, 0, "12", true, 12, "",0, ""),
    Seats(0, 0, "13", false, 13, "",0, ""),
    Seats(0, 0, "14", true, 14, "",0, ""),
    Seats(0, 0, "15", true, 15, "",0, ""),
    Seats(0, 0, "16", false, 16, "",0, ""),
    Seats(0, 0, "17", true, 17, "",0, ""),
    Seats(0, 0, "18", true, 18, "",0, ""),
    Seats(0, 0, "19", false, 19, "",0, ""),
    Seats(0, 0, "20", true, 20, "",0, "")
)
