package kz.busjol.data.remote

data class BookingPost(
    val bookingElements: List<BookingElements>,
    val email: String,
    val phoneNumber: String,
    val phoneTimeAtCreating: String,
    val clientId: Int,
    val segmentId: Int
)

data class BookingElements(
    val iin: String,
    val firstName: String,
    val lastName: String,
    val sex: Int,
    val seatId: Int
)