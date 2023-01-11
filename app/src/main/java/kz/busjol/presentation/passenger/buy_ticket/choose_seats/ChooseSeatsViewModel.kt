package kz.busjol.presentation.passenger.buy_ticket.choose_seats

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

    private var seatsSet = mutableSetOf<Seats>()

    private var seatsIntSet = mutableSetOf<Int>()

    fun onEvent(event: ChooseSeatsEvent) {
        when (event) {
            is ChooseSeatsEvent.AddItemToList -> {
                addItemToSet(event.item)
                setItems()
            }
            is ChooseSeatsEvent.RemoveItem -> {
                removeItemFromSet(event.item)
                setItems()
            }
            is ChooseSeatsEvent.PassPassengersQuantity -> {
                state = state.copy(
                    passengersQuantity = event.quantity
                )
            }
        }
    }

    private fun addItemToSet(item: Seats) {
        val set = seatsSet
        set.add(item)

        val set2 = seatsIntSet
        set2.add(item.seatNumber.toInt())

        seatsSet = set
        seatsIntSet = set2
    }

    private fun removeItemFromSet(item: Seats) {
        val set = seatsSet
        val iterator = seatsSet.iterator()

        while (iterator.hasNext()) {
            if (iterator.next() == item) {
                iterator.remove()
            }
        }

        seatsSet = set

        val set2 = seatsIntSet
        val iterator2 = seatsIntSet.iterator()

        while (iterator2.hasNext()) {
            if (iterator2.next() == item.seatNumber.toInt()) {
                iterator2.remove()
            }
        }

        seatsIntSet = set2
    }

    private fun setItems() {
        val itemWithSeparator = seatsIntSet.toString()
            .replace(",", " /")
            .replace("[", "")
            .replace("]", "")

        val itemWithoutSeparator = seatsIntSet.toString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")

        state = if (seatsIntSet.lastOrNull() == seatsSet.size) {
            state.copy(
                chosenSeatsList = itemWithoutSeparator,
                seatsQuantity = seatsSet.size,
                seatList = seatsSet.toList()
            )
        } else {
            state.copy(
                chosenSeatsList = itemWithSeparator,
                seatsQuantity = seatsSet.size,
                seatList = seatsSet.toList()
            )
        }
    }
}
