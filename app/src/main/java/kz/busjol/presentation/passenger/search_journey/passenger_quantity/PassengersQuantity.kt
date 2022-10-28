package kz.busjol.presentation.passenger.search_journey.passenger_quantity

class PassengersQuantity(
    val adultValue: Int = 1,
    val childValue: Int = 0,
    val disabledValue: Int = 0
) {
    fun allPassengers() = adultValue + childValue + disabledValue
}