package kz.busjol.data.remote

class SeatsDto(
    val row: Int,
    val column: Int,
    val seatNumber: String,
    val isEmpty: Boolean,
    val id: Int,
    val created: String,
    val status: Int,
    val name: String
)