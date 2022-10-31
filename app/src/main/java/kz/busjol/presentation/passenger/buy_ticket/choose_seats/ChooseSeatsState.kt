package kz.busjol.presentation.passenger.buy_ticket.choose_seats

data class ChooseSeatsState(
    val list: String? = null,
    val seatsQuantity: Int? = null
) {

    fun mock() = ChooseSeatsState(
        list = "",
        seatsQuantity = null
    )
}
