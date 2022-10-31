package kz.busjol.presentation.passenger.buy_ticket.passenger_data

data class PassengerDataState(
    val isPassengerHaveLogin: Boolean = false
) {

    fun mock() = PassengerDataState(

    )
}
