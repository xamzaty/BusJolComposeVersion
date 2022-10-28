package kz.busjol.presentation.passenger.journey

sealed interface JourneyEvent {
    data class SelectedOption(val value: Int) : JourneyEvent
}