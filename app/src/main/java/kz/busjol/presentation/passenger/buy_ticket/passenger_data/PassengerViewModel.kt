package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassengerViewModel @Inject constructor(
): ViewModel() {

    var state by mutableStateOf(PassengerDataState().mock())
        private set

    fun onEvent(event: PassengerDataEvent) {
        when (event) {
            else -> {}
        }
    }
}