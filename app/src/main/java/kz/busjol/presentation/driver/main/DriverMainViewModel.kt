package kz.busjol.presentation.driver.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.repository.JourneysRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class DriverMainViewModel @Inject constructor(
    private val journeysRepository: JourneysRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel(){

    var state by mutableStateOf(DriverMainState())
        private set

    init {
        getAppSettings()
    }

    fun onEvent(event: DriverMainEvent) {
        when(event) {
            DriverMainEvent.IsRefreshing -> {
                getAppSettings()
                state = state.copy(isRefreshing = true)
            }
        }
    }

    private fun getAppSettings() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect { result ->
                    getDriverJourneysList(result.userData?.id.orEmpty())
                }
        }
    }

    private fun getDriverJourneysList(driverId: String) {
        viewModelScope.launch {
            journeysRepository
                .getDriverJourneys(driverId)
                .collect {result ->
                    when (result) {
                        is Resource.Success -> {
                            state = state.copy(
                                journeyList = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                journeyList = null,
                                isLoading = false,
                                isRefreshing = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }
}