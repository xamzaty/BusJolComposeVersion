package kz.busjol.presentation.passenger.search_journey

import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City

sealed interface SearchJourneyAction {
    data class SwapCities(val fromCity: City, val toCity: City) : SearchJourneyAction
    data class JourneyListSearch(val journeyPost: JourneyPost) : SearchJourneyAction
    object CityListClicked : SearchJourneyAction
    data class UpdateFromCityValue(val city: City) : SearchJourneyAction
    data class UpdateToCityValue(val city: City) : SearchJourneyAction
}
