package kz.busjol.presentation.passenger.city_picker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class CityPickerViewModel @Inject constructor(
    private val repository: CityListRepository
): ViewModel() {

    var state by mutableStateOf(CityPickerState())
        private set

    init {
        loadCityList()
    }

    private fun loadCityList() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = repository.getCityList()) {
                is Resource.Success -> {
                    state = state.copy(
                        cityList = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        cityList = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}