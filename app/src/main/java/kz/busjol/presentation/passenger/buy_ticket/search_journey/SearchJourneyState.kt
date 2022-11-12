package kz.busjol.presentation.passenger.buy_ticket.search_journey

import kz.busjol.Language
import kz.busjol.domain.models.City
import kz.busjol.domain.models.Journey
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.Passenger
import java.text.SimpleDateFormat
import java.util.*

data class SearchJourneyState(
    val fromCity: City? = null,
    val toCity: City? = null,
    val departureDate: String? = null,
    val arrivalDate: String? = null,
    val language: Language? = null,
    val passengerQuantityList: List<Passenger>? = null,
    val journeyList: List<Journey>? = null,
    val cityList: List<City>? = null,
    val isLoading: Boolean = false,
    val isCityLoading: Boolean = false,
    val isButtonLoading: Boolean = false,
    val error: String? = null,
    val startNewDestination: Boolean? = null
) {

    fun mock(): SearchJourneyState {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val currentDate: String = sdf.format(Date())

        return SearchJourneyState(
            fromCity = City(),
            toCity = City(),
            departureDate = currentDate,
            arrivalDate = currentDate,
            passengerQuantityList = listOf(Passenger()),
            journeyList = emptyList(), cityList = emptyList(),
            language = Language.RUSSIAN
        )
    }
}