package kz.busjol.presentation.passenger.search_journey.city_picker

sealed class CityPickerAction {
    data class PassCityValueOnSearch(val value: String): CityPickerAction()
}