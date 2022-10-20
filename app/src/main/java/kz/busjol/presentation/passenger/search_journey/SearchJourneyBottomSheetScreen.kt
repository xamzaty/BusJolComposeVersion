package kz.busjol.presentation.passenger.search_journey

sealed class SearchJourneyBottomSheetScreen() {
    object PassengersScreen: SearchJourneyBottomSheetScreen()
    class CityPickerScreen (val argument: String):SearchJourneyBottomSheetScreen()
}
