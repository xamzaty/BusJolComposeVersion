package kz.busjol.presentation.passenger.buy_ticket.journey

sealed interface JourneyEvent {
    data class SelectedOption(val value: Int) : JourneyEvent
}