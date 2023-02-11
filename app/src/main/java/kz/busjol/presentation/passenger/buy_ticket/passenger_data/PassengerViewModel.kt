package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.UserState
import kz.busjol.data.remote.BookingPost
import kz.busjol.domain.models.Booking
import kz.busjol.domain.repository.BookingListRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class PassengerViewModel @Inject constructor(
    private val bookingListRepository: BookingListRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var state by mutableStateOf(PassengerDataState().mock())
        private set

    private var emailValue: String = ""
    private var phoneValue: String = ""

    init {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    emailValue = it.userData?.email ?: ""
                    phoneValue = it.userData?.phone ?: ""

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
                state = state.copy(
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

    private fun loadBookingList(bookingPost: BookingPost) {
        viewModelScope.launch {
            bookingListRepository
                .getBookingList(
                    if (state.isPassengerHaveLogin) {
                        BookingPost(
                            bookingElements = bookingPost.bookingElements,
                            email = emailValue,
                            phoneNumber = phoneValue,
                            phoneTimeAtCreating = bookingPost.phoneTimeAtCreating,
                            clientId = 0,
                            segmentId = bookingPost.segmentId
                        )
                    } else bookingPost
                )
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
                                booking = mockData(),
                                isLoading = false,
                                error = result.message,
                                startNewDestination = true
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

private fun mockData(): List<Booking> = listOf(
    Booking(
        id = 1,
        qrCode = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        seatNumber = "1",
        departsFrom = "Алматы",
        arrivesTo = "Балхаш",
        departsAt = "2023",
        arrivesAt = "2023",
        clientInfo = ""
    ),

    Booking(
        id = 1,
        qrCode = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        seatNumber = "1",
        departsFrom = "Алматы",
        arrivesTo = "Балхаш",
        departsAt = "2023",
        arrivesAt = "2023",
        clientInfo = ""
    ),

    Booking(
        id = 1,
        qrCode = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        seatNumber = "1",
        departsFrom = "Алматы",
        arrivesTo = "Балхаш",
        departsAt = "2023",
        arrivesAt = "2023",
        clientInfo = ""
    )
)