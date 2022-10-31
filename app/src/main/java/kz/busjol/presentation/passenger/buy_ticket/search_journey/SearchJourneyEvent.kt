package kz.busjol.presentation.passenger.buy_ticket.search_journey

import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.Passenger
import java.util.Date

sealed interface SearchJourneyEvent {
    data class SwapCities(val fromCity: City, val toCity: City) : SearchJourneyEvent
    data class JourneyListSearch(val journeyPost: JourneyPost) : SearchJourneyEvent
    data class UpdateFromCityValue(val city: City) : SearchJourneyEvent
    data class UpdateToCityValue(val city: City) : SearchJourneyEvent
    data class UpdateDateValue(val date: String) : SearchJourneyEvent
    data class UpdatePassengersQuantityValue(val passengersQuantity: List<Passenger>) : SearchJourneyEvent
    data class NewDestinationStatus(val isStarted: Boolean) : SearchJourneyEvent
    data class AdultPassengerQuantity(val quantity: Int) : SearchJourneyEvent
    data class ChildPassengerQuantity(val quantity: Int) : SearchJourneyEvent
    data class DisabledPassengerQuantity(val quantity: Int) : SearchJourneyEvent
    object CityListClicked : SearchJourneyEvent
}
