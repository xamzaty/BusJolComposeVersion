package kz.busjol.presentation.passenger.search_journey

import kz.busjol.domain.models.City
import kz.busjol.domain.models.Journey

data class SearchJourneyState(
    val fromCity: City? = null,
    val toCity: City? = null,
    val date: String? = null,
    val passengerQuantity: String? = null,
    val journeyList: List<Journey>? = null,
    val cityList: List<City>? = null,
    val isLoading: Boolean = false,
    val isCityLoading: Boolean = false,
    val isButtonLoading: Boolean = false,
    val error: String? = null
)