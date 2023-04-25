package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import kz.busjol.data.remote.BookingElements
import kz.busjol.data.remote.BookingPost

sealed interface PassengerDataEvent {
    data class OnContinueButtonAction(val bookingPost: BookingPost): PassengerDataEvent

    data class OnPassengerValueUpdate(val bookingPost: BookingElements, val id: Int) : PassengerDataEvent
}