package kz.busjol.presentation.passenger.buy_ticket.journey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JourneyViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(JourneyState().mock())
        private set

    fun onEvent(event: JourneyEvent) {
        when (event) {
            is JourneyEvent.SelectedOption -> {
                state = state.copy(
                    selectedOption = state.selectedOption
                )
            }
        }
    }
}
