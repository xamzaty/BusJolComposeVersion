package kz.busjol.presentation.passenger.buy_ticket.choose_seats

data class ChooseSeatsState(
    val list: String? = null
) {

    fun mock() = ChooseSeatsState(
        list = ""
    )
}
