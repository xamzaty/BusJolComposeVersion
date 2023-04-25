package kz.busjol.data.remote.api

import kz.busjol.data.remote.TicketInfoDto
import retrofit2.http.POST
import retrofit2.http.Query

interface TicketsApi {

    @POST("Tickets/Driver/CheckTicket")
    suspend fun checkTicket(
        @Query("qrCode") qrCode: String
    ): List<TicketInfoDto>
}