package kz.busjol.data.remote

class TicketInfoDto(
    val id: Int,
    val qrCode: String,
    val seatNumber: String,
    val departsFrom: String,
    val arrivesTo: String,
    val departsAt: String,
    val arrivesAt: String,
    val clientInfo: String
)