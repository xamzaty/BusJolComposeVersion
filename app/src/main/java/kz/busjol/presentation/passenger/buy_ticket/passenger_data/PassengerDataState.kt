package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import kz.busjol.data.remote.BookingElements
import kz.busjol.domain.models.Booking

data class PassengerDataState(
    val isPassengerHaveLogin: Boolean = false,
    val isPassengerListDataFilled: Boolean = false,
    val isPassengerDataFilled: Boolean = false,
    val isLoading: Boolean = false,
    val booking: List<Booking>? = null,
    val error: String? = null,
    val passengerList: List<BookingElements>? = null,
    val bookingElementsList: List<BookingElements>? = null,
    val startNewDestination: Boolean = false,
    val setDataToList: Boolean = false
) {

    fun mock() = PassengerDataState(
        isPassengerHaveLogin = false,
        isPassengerListDataFilled = false,
        isPassengerDataFilled = false,
        isLoading = false,
        booking = null,
        error = null,
        bookingElementsList = null,
        startNewDestination = false,
        setDataToList = false
    )
}
