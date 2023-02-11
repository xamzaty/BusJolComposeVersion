package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import kz.busjol.data.remote.BookingElements
import kz.busjol.domain.models.Booking

data class PassengerDataState(
    val isPassengerHaveLogin: Boolean = false,
    val isPhoneDataFilled: Boolean = false,
    val isEmailDataFilled: Boolean = false,
    val isPassengerDataFilled: Boolean = false,
    val isPassengerListDataFilled: Boolean = false,
    val isLoading: Boolean = false,
    val booking: List<Booking>? = null,
    val error: String? = null,
    val passengerList: List<BookingElements>? = null,
    val startNewDestination: Boolean = false,
    val setDataToList: Boolean = false,
    val isAllDataSet: Boolean = false
) {

    fun mock() = PassengerDataState(
        isPassengerHaveLogin = false,
        isPassengerListDataFilled = false,
        isPassengerDataFilled = false,
        isLoading = false,
        booking = null,
        error = null,
        startNewDestination = false,
        setDataToList = false
    )
}
