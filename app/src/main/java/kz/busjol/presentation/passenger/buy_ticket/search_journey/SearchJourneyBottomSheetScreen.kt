package kz.busjol.presentation.passenger.buy_ticket.search_journey

sealed class SearchJourneyBottomSheetScreen() {
    object PassengersScreen: SearchJourneyBottomSheetScreen()
    object CalendarScreen: SearchJourneyBottomSheetScreen()
    class CityPickerScreen (val argument: String): SearchJourneyBottomSheetScreen()
}
