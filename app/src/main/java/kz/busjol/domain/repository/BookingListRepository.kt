package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.data.remote.BookingPost
import kz.busjol.domain.models.Booking
import kz.busjol.domain.util.Resource

interface BookingListRepository {
    suspend fun getBookingList(bookingPost: BookingPost): Flow<Resource<List<Booking>>>
}