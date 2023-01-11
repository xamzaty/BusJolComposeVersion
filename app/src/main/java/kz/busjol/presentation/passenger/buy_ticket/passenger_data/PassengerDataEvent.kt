package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import kz.busjol.data.remote.BookingElements
import kz.busjol.data.remote.BookingPost

sealed interface PassengerDataEvent {
    data class OnContinueButtonAction(val bookingPost: BookingPost): PassengerDataEvent
    data class PassengerData(val bookingElements: BookingElements): PassengerDataEvent
    data class SetDataToListStatus(var dataToListStatus: Boolean): PassengerDataEvent
}