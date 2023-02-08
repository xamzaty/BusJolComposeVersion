package kz.busjol.presentation.passenger.buy_ticket.search_journey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kz.busjol.BuildConfig
import kz.busjol.Language
import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City
import kz.busjol.domain.models.Journey
import kz.busjol.domain.models.JourneyItem
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.util.Resource
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.Passenger
import javax.inject.Inject

@HiltViewModel
class SearchJourneyViewModel @Inject constructor(
    private val cityRepository: CityListRepository,
    private val journeyRepository: JourneyListRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var state by mutableStateOf(SearchJourneyState().mock())
        private set

    private var fromCity by mutableStateOf(City())
    private var toCity by mutableStateOf(City())

    init {
        getLanguage()
        loadCityList(true)
    }

    fun onEvent(event: SearchJourneyEvent) {
        when(event) {
            is SearchJourneyEvent.SwapCities -> onSwapCities(event.fromCity, event.toCity)
            is SearchJourneyEvent.JourneyListSearch -> loadJourneyList(event.journeyPost)
            is SearchJourneyEvent.CityListClicked -> loadCityList(isInit = false)
            is SearchJourneyEvent.UpdateFromCityValue -> updateFromCityValue(event.city)
            is SearchJourneyEvent.UpdateToCityValue -> updateToCityValue(event.city)
            is SearchJourneyEvent.UpdateDateValue -> updateDateValue(event.date)
            is SearchJourneyEvent.UpdatePassengersQuantityValue -> updatePassengersQuantityValue(event.passengersQuantity)
            is SearchJourneyEvent.NewDestinationStatus -> newDestinationStatus(event.isStarted)
            is SearchJourneyEvent.SetLanguage -> setLanguage(event.language)
        }
    }

    private fun getLanguage() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    state = state.copy(
                        language = it.language
                    )
            }
        }
    }

    private fun setLanguage(language: Language) {
        viewModelScope.launch {
            dataStoreRepository.setLanguage(language)
        }
    }

    private fun loadCityList(isInit: Boolean) {
        viewModelScope.launch {
            cityRepository
                .getCityList()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            fromCity = result.data?.get(0) ?: City()
                            toCity = result.data?.get(1) ?: City()

                            state = if (isInit) {
                                state.copy(
                                    fromCity = fromCity,
                                    toCity = toCity,
                                    isLoading = false,
                                    error = null
                                )
                            } else {
                                state.copy(
                                    cityList = result.data.orEmpty(),
                                    isCityLoading = false,
                                    error = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            state = if (isInit) {
                                state.copy(
                                    fromCity = null,
                                    toCity = null,
                                    isLoading = false,
                                    error = result.message
                                )
                            } else {
                                state.copy(
                                    cityList = null,
                                    isCityLoading = false,
                                    error = result.message
                                )
                            }
                        }

                        is Resource.Loading -> {
                            state = if (isInit) {
                                state.copy(isLoading = result.isLoading)
                            } else {
                                state.copy(isCityLoading = result.isLoading)
                            }
                        }
                    }
                }
        }
    }

    private fun onSwapCities(fromCity: City, toCity: City) {
        state = state.copy(
            fromCity = toCity,
            toCity = fromCity,
            isLoading = false,
            error = null
        )
    }

    private fun updateFromCityValue(city: City) {
        state = state.copy(
            fromCity = city
        )
    }

    private fun updateToCityValue(city: City) {
        state = state.copy(
            toCity = city
        )
    }

    private fun updateDateValue(arrivalDateValue: String) {
        state = state.copy(
            arrivalDate = arrivalDateValue
        )
    }

    private fun updatePassengersQuantityValue(passengersQuantity: List<Passenger>) {
        state = state.copy(
            passengerQuantityList = passengersQuantity
        )
    }

    private fun newDestinationStatus(isStarted: Boolean) {
        state = state.copy(
            startNewDestination = isStarted
        )
    }

    private fun loadJourneyList(journeyPost: JourneyPost) {
        viewModelScope.launch {
            val allPassengers = journeyPost.adultAmount + journeyPost.childrenAmount + journeyPost.disabledAmount

            println(allPassengers)

            val journeyMock = listOf(
                Journey(
                journey = JourneyItem(1, "", 1, "", "", 1, 1, 1, "", 1),
                amount = 3000, arrivalTime = "", cityFrom = City(0, "Алматы"), cityTo = City(1, "Балхаш"),
                departureTime = "", numberOfFreePlaces = 10, numberOfPlaces = 100, segmentId = 1, stopName = "Сидячий"
            ),
                Journey(
                    journey = JourneyItem(1, "", 1, "", "", 1, 1, 1, "", 1),
                    amount = 3000, arrivalTime = "", cityFrom = City(0, "Алматы"), cityTo = City(1, "Балхаш"),
                    departureTime = "", numberOfFreePlaces = 5, numberOfPlaces = 100, segmentId = 1, stopName = "Лежачий"
                ),

                Journey(
                    journey = JourneyItem(1, "", 1, "", "", 1, 1, 1, "", 1),
                    amount = 3000, arrivalTime = "", cityFrom = City(0, "Алматы"), cityTo = City(1, "Балхаш"),
                    departureTime = "", numberOfFreePlaces = 3, numberOfPlaces = 100, segmentId = 1, stopName = "Сидячий"
                ),

                Journey(
                    journey = JourneyItem(1, "", 1, "", "", 1, 1, 1, "", 1),
                    amount = 3000, arrivalTime = "", cityFrom = City(0, "Алматы"), cityTo = City(1, "Балхаш"),
                    departureTime = "", numberOfFreePlaces = 2, numberOfPlaces = 100, segmentId = 1, stopName = "Сидячий"
                ),

                Journey(
                    journey = JourneyItem(1, "", 1, "", "", 1, 1, 1, "", 1),
                    amount = 3000, arrivalTime = "", cityFrom = City(0, "Алматы"), cityTo = City(1, "Балхаш"),
                    departureTime = "", numberOfFreePlaces = 1, numberOfPlaces = 100, segmentId = 1, stopName = "Лежачий"
                ),
            )

            journeyRepository
                .getJourneyList(journeyPost)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            state = state.copy(
                                journeyList = result.data.orEmpty(),
                                isButtonLoading = false,
                                startNewDestination = true,
                                error = null
                            )

                            if (result.data?.isEmpty() == true && BuildConfig.DEBUG) {
                                state = state.copy(
                                    journeyList = journeyMock,
                                    isButtonLoading = false,
                                    startNewDestination = true,
                                    error = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            state = state.copy(
                                journeyList = null,
                                isButtonLoading = false,
                                startNewDestination = false,
                                error = result.message
                            )
                        }

                        is Resource.Loading -> {
                            state = state.copy(
                                isButtonLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }
}
