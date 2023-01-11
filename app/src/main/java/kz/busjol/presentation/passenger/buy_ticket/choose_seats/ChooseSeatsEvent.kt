package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import kz.busjol.domain.models.Seats

sealed interface ChooseSeatsEvent {
    data class AddItemToList(val item: Seats) : ChooseSeatsEvent
    data class RemoveItem(val item: Seats) : ChooseSeatsEvent
    data class PassPassengersQuantity(val quantity: Int) : ChooseSeatsEvent
}