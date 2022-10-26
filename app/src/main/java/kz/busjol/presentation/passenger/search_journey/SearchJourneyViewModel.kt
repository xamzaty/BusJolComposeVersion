package kz.busjol.presentation.passenger.search_journey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class SearchJourneyViewModel @Inject constructor(
    private val cityRepository: CityListRepository,
    private val journeyRepository: JourneyListRepository
) : ViewModel() {

    var state by mutableStateOf(SearchJourneyState())
        private set

    var fromCity by mutableStateOf(City())
        private set

    var toCity by mutableStateOf(City())
        private set

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = cityRepository.getCityList()) {
                is Resource.Success -> {
                    fromCity = result.data?.get(0) ?: City()
                    toCity = result.data?.get(1) ?: City()

                    state = state.copy(
                        fromCity = fromCity,
                        toCity = toCity,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        fromCity = null,
                        toCity = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    private fun loadCityList() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = cityRepository.getCityList()) {
                is Resource.Success -> {
                    state = state.copy(
                        cityList = result.data.orEmpty(),
                        isCityLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        cityList = null,
                        isCityLoading = false,
                        error = result.message
                    )
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

    private fun loadJourneyList(journeyPost: JourneyPost) {
        viewModelScope.launch {
            state = state.copy(
                isButtonLoading = true,
                error = null
            )
            when (val result = journeyRepository.getJourneyList(journeyPost)) {
                is Resource.Success -> {
                    state = state.copy(
                        journeyList = result.data,
                        isButtonLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        journeyList = result.data,
                        isButtonLoading = false,
                        error = result.message
                    )
                }
            }
        }
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

    fun onAction(action: SearchJourneyAction) {
        when(action) {
           is SearchJourneyAction.SwapCities -> onSwapCities(action.fromCity, action.toCity)
           is SearchJourneyAction.JourneyListSearch -> loadJourneyList(action.journeyPost)
           is SearchJourneyAction.CityListClicked -> loadCityList()
           is SearchJourneyAction.UpdateFromCityValue -> updateFromCityValue(action.city)
           is SearchJourneyAction.UpdateToCityValue -> updateToCityValue(action.city)
        }
    }
}