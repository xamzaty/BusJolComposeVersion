package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.UserState
import kz.busjol.data.remote.BookingElements
import kz.busjol.data.remote.BookingPost
import kz.busjol.domain.repository.BookingListRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class PassengerViewModel @Inject constructor(
    private val bookingListRepository: BookingListRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    var state by mutableStateOf(PassengerDataState().mock())
        private set

    private var bookingList = mutableSetOf<BookingElements>()

    init {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    state = state.copy(
                        isPassengerHaveLogin = it.userState == UserState.REGISTERED
                    )
                }
        }
    }

    fun onEvent(event: PassengerDataEvent) {
        when (event) {
            is PassengerDataEvent.OnContinueButtonAction -> {
                loadBookingList(event.bookingPost)
            }
            is PassengerDataEvent.PassengerData -> {
                addItemToSet(event.bookingElements)
                state = state.copy(
                    bookingElementsList = bookingList.toList(),
                    setDataToList = true
                )
            }
            is PassengerDataEvent.SetDataToListStatusFalse -> {
                state = state.copy(
                    setDataToList = false
                )
            }
            is PassengerDataEvent.SetDataToListStatusTrue -> {
                state = state.copy(
                    setDataToList = true
                )
            }
        }
    }

    private fun addItemToSet(item: BookingElements) {
        val list = bookingList
        list.addAll(setOf(item))

        bookingList = list
    }

    private fun loadBookingList(bookingPost: BookingPost) {
        viewModelScope.launch {
            bookingListRepository
                .getBookingList(bookingPost)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            state = state.copy(
                                booking = result.data,
                                isLoading = false,
                                error = null,
                                startNewDestination = true
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                booking = null,
                                isLoading = false,
                                error = result.message,
                                startNewDestination = false
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = true
                            )
                        }
                    }
                }
        }
    }
}