package kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.busjol.domain.models.Seats

@Parcelize
data class Passenger(
    val type: PassengerType? = PassengerType.ADULT,
    val iin: String? = "",
    val lastName: String? = "",
    val firstName: String? = "",
    val sex: Int = 0,
    val seatId: Int? = null
): Parcelable {

    enum class PassengerType {
        ADULT, CHILD, DISABLED
    }
}
