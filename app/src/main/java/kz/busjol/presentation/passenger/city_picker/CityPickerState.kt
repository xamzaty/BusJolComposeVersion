package kz.busjol.presentation.passenger.city_picker

import kz.busjol.domain.models.City

data class CityPickerState(
    val cityList: List<City>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)