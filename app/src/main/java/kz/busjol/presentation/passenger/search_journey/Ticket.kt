package kz.busjol.presentation.passenger.search_journey

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.busjol.domain.models.City
import kz.busjol.domain.models.Journey
import kz.busjol.presentation.passenger.search_journey.passenger_quantity.Passenger

@Parcelize
data class Ticket(
    val departureCity: City? = null,
    val arrivalCity: City? = null,
    val date: String? = null,
    val passengerList: List<Passenger>? = null,
    val journeyList: List<Journey>? = null,
    val journey: Journey? = null
): Parcelable