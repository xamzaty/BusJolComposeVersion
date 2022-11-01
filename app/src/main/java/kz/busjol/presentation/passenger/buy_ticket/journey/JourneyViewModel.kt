package kz.busjol.presentation.passenger.buy_ticket.journey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
                    selectedOption = state.selectedOption
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
