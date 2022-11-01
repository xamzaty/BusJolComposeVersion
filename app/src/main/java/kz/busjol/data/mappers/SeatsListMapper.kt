package kz.busjol.data.mappers

import kz.busjol.data.remote.SeatsDto
import kz.busjol.domain.models.Seats

fun List<SeatsDto>.toSeatsList() = this.map { seats ->
    Seats(
        row = seats.row,
        column = seats.column,
        seatNumber = seats.seatNumber,
        isEmpty = seats.isEmpty,
        id = seats.id,
        created = seats.created,
        status = seats.status,
        name = seats.name
    )
}