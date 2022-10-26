package kz.busjol.data.remote

class JourneyPost(
    val cityFrom: Int,
    val cityTo: Int,
    val dateFrom: String,
    val dateTo: String,
    val childrenAmount: Int,
    val adultAmount: Int,
    val disabledAmount: Int
)