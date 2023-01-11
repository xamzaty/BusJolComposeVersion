package kz.busjol.data.remote.api

import kz.busjol.data.remote.BookingDto
import kz.busjol.data.remote.BookingPost
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApi {

    @POST("Booking")
    suspend fun getBookingItem(
        @Body body: BookingPost
    ): List<BookingDto>
}