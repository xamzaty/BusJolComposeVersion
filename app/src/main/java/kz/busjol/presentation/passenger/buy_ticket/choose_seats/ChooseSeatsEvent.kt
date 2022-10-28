package kz.busjol.presentation.passenger.buy_ticket.choose_seats

sealed interface ChooseSeatsEvent {
    data class AddItemToList(val item: Int) : ChooseSeatsEvent
    data class RemoveItem(val item: Int) : ChooseSeatsEvent
}