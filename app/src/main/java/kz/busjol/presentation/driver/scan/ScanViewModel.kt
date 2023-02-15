package kz.busjol.presentation.driver.scan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.domain.repository.TicketsRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val ticketsRepository: TicketsRepository
): ViewModel() {

    var state by mutableStateOf(ScanState())
        private set

    fun onEvent(event: ScanEvent) {
        when(event) {
            is ScanEvent.CheckTicket -> checkTicket(event.qrCode)
        }
    }

    private fun checkTicket(qrCode: String) {
        viewModelScope.launch {
            ticketsRepository
                .checkTicket(qrCode)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            state = state.copy(
                                isTicketValid = true,
                                isLoading = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isTicketValid = false,
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = result.isLoading,
                            )
                        }
                    }
                }
        }
    }
}