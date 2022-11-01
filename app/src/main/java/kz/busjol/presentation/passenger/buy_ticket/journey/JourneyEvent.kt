package kz.busjol.presentation.passenger.buy_ticket.journey

import kz.busjol.domain.models.Journey

sealed interface JourneyEvent {
    data class SelectedOption(val value: Int) : JourneyEvent
    data class OnJourneyClicked(val id: String, val selectedJourney: Journey) : JourneyEvent
    data class NewDestinationStatus(val isStarted: Boolean) : JourneyEvent
}