package kz.busjol.presentation.passenger.buy_ticket.journey

import kz.busjol.domain.models.Journey
import kz.busjol.domain.models.Seats

data class JourneyState(
    val selectedJourney: Journey? = null,
    val selectedOption: Int? = null,
    val seatsList: List<Seats>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val startNewDestination: Boolean = false
) {

    fun mock() = JourneyState(
        selectedJourney = null,
        selectedOption = 0,
        seatsList = null,
        isLoading = false,
        error = null,
        startNewDestination = false
    )
}
