package kz.busjol.data.mappers

import kz.busjol.data.remote.TicketInfoDto
import kz.busjol.domain.models.TicketInfo

fun List<TicketInfoDto>.toTicketInfoDomain() = this.map { ticket ->
    TicketInfo(
        id = ticket.id,
        qrCode = ticket.qrCode,
        seatNumber = ticket.seatNumber,
        departsFrom = ticket.departsFrom,
        arrivesTo = ticket.arrivesTo,
        departsAt = ticket.departsAt,
        arrivesAt = ticket.arrivesAt,
        clientInfo = ticket.clientInfo
    )
}