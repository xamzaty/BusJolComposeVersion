package kz.busjol.presentation.passenger.my_tickets

sealed interface MyTicketsEvent {
    object IsRefreshing : MyTicketsEvent
}