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

    private var seatsSet = mutableStateOf(mutableSetOf<Int>())

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
        val set = seatsSet.value
        set.add(item)

        seatsSet.value = set
    }

    private fun removeItemFromSet(item: Int) {
        val set = seatsSet.value
        val iterator = seatsSet.value.iterator()

        while (iterator.hasNext()) {
            if (iterator.next() == item) {
                iterator.remove()
            }
        }

        seatsSet.value = set
    }

    private fun setItems() {
        val itemWithSeparator = seatsSet.value.toString()
            .replace(",", " /")
            .replace("[", "")
            .replace("]", "")

        val itemWithoutSeparator = seatsSet.value.toString()
            .replace(",", "")
            .replace("[", "")
            .replace("]", "")

        state = if (seatsSet.value.lastOrNull() == seatsSet.value.size) {
            state.copy(
                list = itemWithoutSeparator,
                seatsQuantity = seatsSet.value.size
            )
        } else {
            state.copy(
                list = itemWithSeparator,
                seatsQuantity = seatsSet.value.size
            )
        }
    }
}
