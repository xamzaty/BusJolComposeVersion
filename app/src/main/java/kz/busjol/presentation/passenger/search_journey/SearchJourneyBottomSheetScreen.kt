package kz.busjol.presentation.passenger.search_journey

sealed class SearchJourneyBottomSheetScreen() {
    object PassengersScreen: SearchJourneyBottomSheetScreen()
    object CalendarScreen: SearchJourneyBottomSheetScreen()
    class CityPickerScreen (val argument: String):SearchJourneyBottomSheetScreen()
}
