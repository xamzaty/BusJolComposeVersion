package kz.busjol.data.mappers

import kz.busjol.data.remote.BookingDto
import kz.busjol.domain.models.Booking

fun List<BookingDto>.toBookingList() = this.map { booking ->
    Booking(
        id = booking.id,
        qrCode = booking.qrCode,
        seatNumber = booking.seatNumber,
        departsFrom = booking.departsFrom,
        arrivesTo = booking.arrivesTo,
        departsAt = booking.departsAt,
        arrivesAt = booking.arrivesAt,
        clientInfo = booking.clientInfo
    )
}