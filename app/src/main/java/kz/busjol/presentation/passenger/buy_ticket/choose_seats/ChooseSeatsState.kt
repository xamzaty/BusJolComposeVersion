package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import kz.busjol.domain.models.Seats

data class ChooseSeatsState(
    val chosenSeatsList: String? = null,
    val seatList: List<Seats>? = null,
    val seatsQuantity: Int? = null,
    val passengersQuantity: Int? = null
) {

    fun mock() = ChooseSeatsState(
        chosenSeatsList = "",
        seatsQuantity = null,
        passengersQuantity = null
    )
}
