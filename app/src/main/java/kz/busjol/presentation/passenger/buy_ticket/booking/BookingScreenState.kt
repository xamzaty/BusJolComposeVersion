package kz.busjol.presentation.passenger.buy_ticket.booking

data class BookingScreenState(
    val countdownTimerValue: String? = null,
    val isTimeExpired: Boolean = false
)
