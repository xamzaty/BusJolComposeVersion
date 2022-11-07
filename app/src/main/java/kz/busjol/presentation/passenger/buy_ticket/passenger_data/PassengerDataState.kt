package kz.busjol.presentation.passenger.buy_ticket.passenger_data

data class PassengerDataState(
    val isPassengerHaveLogin: Boolean = false,
    val isPassengerListDataFilled: Boolean = false,
    val isPassengerDataFilled: Boolean = false
) {

    fun mock() = PassengerDataState(
        isPassengerHaveLogin = false,
        isPassengerListDataFilled = false,
        isPassengerDataFilled = false
    )
}
