package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.busjol.domain.models.Seats
import javax.inject.Inject

@HiltViewModel
class ChooseSeatsViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(ChooseSeatsState().mock())
        private set

    private val seatsSet = mutableSetOf<Seats>()
    private val seatsIntSet = mutableSetOf<Int>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun onEvent(event: ChooseSeatsEvent) {
        when (event) {
            is ChooseSeatsEvent.AddItemToList -> {
                addItemToSet(event.item)
                updateItems()
            }
            is ChooseSeatsEvent.RemoveItem -> {
                removeItemFromSet(event.item)
                updateItems()
            }
            is ChooseSeatsEvent.PassPassengersQuantity -> {
                state = state.copy(passengersQuantity = event.quantity)
            }
        }
    }

    private fun addItemToSet(item: Seats) {
        seatsSet.add(item)
        seatsIntSet.add(item.seatNumber.toInt())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeItemFromSet(item: Seats) {
        seatsSet.removeIf { it == item }
        seatsIntSet.remove(item.seatNumber.toInt())
    }

    private fun updateItems() {
        val sortedSeatsIntSet = seatsIntSet.sorted()
        val itemWithSeparator = sortedSeatsIntSet.joinToString(" / ")

        state = state.copy(
            chosenSeatsList = itemWithSeparator,
            seatsQuantity = seatsSet.size,
            seatList = seatsSet.toList()
        )
    }
}
