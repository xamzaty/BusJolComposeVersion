package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseSeatsViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(ChooseSeatsState().mock())
        private set

    private var seatsSet = mutableSetOf<Int>()

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
        }
    }

    private fun addItemToSet(item: Int) {
        val set = seatsSet
        set.add(item)

        seatsSet = set
    }

    private fun removeItemFromSet(item: Int) {
        val set = seatsSet
        val iterator = seatsSet.iterator()

        while (iterator.hasNext()) {
            if (iterator.next() == item) {
                iterator.remove()
            }
        }

        seatsSet = set
    }

    private fun setItems() {
        val itemWithSeparator = seatsSet.toString()
            .replace(",", " /")
            .replace("[", "")
            .replace("]", "")

        val itemWithoutSeparator = seatsSet.toString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")

        state = if (seatsSet.lastOrNull() == seatsSet.size) {
            state.copy(
                list = itemWithoutSeparator,
                seatsQuantity = seatsSet.size
            )
        } else {
            state.copy(
                list = itemWithSeparator,
                seatsQuantity = seatsSet.size
            )
        }
    }
}
