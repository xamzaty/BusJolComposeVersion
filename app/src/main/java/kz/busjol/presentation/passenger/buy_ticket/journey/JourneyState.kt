package kz.busjol.presentation.passenger.buy_ticket.journey

import kz.busjol.domain.models.Journey

data class JourneyState(
    val selectedJourney: Journey? = null,
    val selectedOption: Int? = null
) {
    fun mock() = JourneyState(
        selectedJourney = null,
        selectedOption = 0
    )
}
