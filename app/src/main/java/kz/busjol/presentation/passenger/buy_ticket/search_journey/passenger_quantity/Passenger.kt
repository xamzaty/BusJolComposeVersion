package kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Passenger(
    val type: PassengerType? = PassengerType.ADULT,
    val gender: PassengerGender? = null,
    val iin: String? = "",
    val lastName: String? = "",
    val firstName: String? = "",
    val birthDate: String? = ""
): Parcelable {

    fun isChild() = type == PassengerType.CHILD

    fun isDisabled() = type == PassengerType.DISABLED

    enum class PassengerType {
        ADULT, CHILD, DISABLED
    }

    enum class PassengerGender {
        MEN, FEMALE
    }
}
