package kz.busjol.presentation.passenger.search_journey.city_picker

import kz.busjol.domain.models.City

data class CityPickerState(
    val cityList: List<City>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)